package org.example.backend.Service.Impl;

import org.example.backend.Entity.pojo.Conversation;
import org.example.backend.Entity.pojo.Message;
import org.example.backend.Entity.vo.MessageVO;
import org.example.backend.Service.AIService;
import org.example.backend.Service.ConversationService;
import org.example.backend.Service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 消息服务实现类
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private AIService aiService;

    // 移除字段注入
    private ConversationService conversationService;

    // 使用构造器注入，添加@Lazy注解来打破循环依赖
    @Autowired
    public void setConversationService(@Lazy ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    // TODO: 添加消息数据访问层依赖
    
    // 摘要生成触发条件 - 消息数阈值
    private static final int SUMMARY_THRESHOLD = 10;

    @Override
    public List<MessageVO> getConversationMessages(Integer conversationId, Integer userId) {
        // 验证用户是否有权限访问这个对话
        if (conversationService.getConversation(conversationId, userId) == null) {
            return new ArrayList<>();
        }

        // TODO: 从数据库获取消息并转换为VO
        // 临时返回空列表，等待数据访问层实现
        return new ArrayList<>();
    }

    @Override
    @Transactional
    public MessageVO sendMessage(Integer conversationId, String content, Integer userId) {
        try {
            // 验证用户是否有权限访问这个对话
            if (conversationService.getConversation(conversationId, userId) == null) {
                return null;
            }

            // 创建用户消息
            Message userMessage = new Message(conversationId, "user", content, new Date());
            Integer messageId = saveMessage(userMessage);
            
            // 更新对话状态
            updateConversationStatus(conversationId, userMessage);

            // 调用AI服务获取回复
            String aiResponse = aiService.chat(userId.toString(), content);

            // 创建AI回复消息
            Message assistantMessage = new Message(conversationId, "assistant", aiResponse, new Date());
            Integer assistantMessageId = saveMessage(assistantMessage);
            
            // 更新对话状态
            updateConversationStatus(conversationId, assistantMessage);
            
            // 检查是否需要更新摘要
            checkAndUpdateSummary(conversationId, userId);

            // 返回AI回复的VO
            MessageVO messageVO = new MessageVO();
            messageVO.setId(assistantMessageId.toString());
            messageVO.setRole("assistant");
            messageVO.setContent(aiResponse);
            messageVO.setTimestamp(assistantMessage.getTimestamp());
            
            return messageVO;
        } catch (Exception e) {
            // 处理异常，可以记录日志
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Integer saveMessage(Message message) {
        // TODO: 实现消息保存逻辑
        // 临时返回null，等待数据访问层实现
        return 1; // 模拟ID
    }

    @Override
    public boolean deleteConversationMessages(Integer conversationId) {
        // TODO: 实现删除对话消息的逻辑
        // 临时返回true，等待数据访问层实现
        return true;
    }
    
    @Override
    @Transactional
    public boolean updateConversationStatus(Integer conversationId, Message message) {
        try {
            // 获取当前对话
            Conversation conversation = conversationService.getConversation(conversationId, null);
            
            if (conversation != null) {
                // 更新最后消息预览（截取内容，避免过长）
                String preview = message.getContent();
                if (preview.length() > 50) {
                    preview = preview.substring(0, 47) + "...";
                }
                conversation.setLastMessage(preview);
                
                // 更新消息数量
                Integer totalMessages = conversation.getTotalMessages();
                if (totalMessages == null) totalMessages = 0;
                conversation.setTotalMessages(totalMessages + 1);
                
                // 更新对话时间
                conversation.setUpdatedAt(new Date());
                
                // 保存更新
                return conversationService.updateConversation(conversation);
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean checkAndUpdateSummary(Integer conversationId, Integer userId) {
        try {
            // 获取当前对话
            Conversation conversation = conversationService.getConversation(conversationId, userId);
            
            if (conversation != null) {
                Integer totalMessages = conversation.getTotalMessages();
                
                // 当消息数是SUMMARY_THRESHOLD的倍数时，触发摘要生成
                // 或者当对话没有摘要时也生成
                if (totalMessages != null && 
                    (totalMessages % SUMMARY_THRESHOLD == 0 || 
                     conversation.getSummary() == null || 
                     conversation.getSummary().isEmpty())) {
                     
                    // 异步生成摘要
                    new Thread(() -> {
                        try {
                            conversationService.generateAndUpdateSummary(conversationId, userId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }).start();
                    
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
} 