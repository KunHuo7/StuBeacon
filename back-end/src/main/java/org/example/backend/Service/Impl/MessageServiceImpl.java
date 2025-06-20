package org.example.backend.Service.Impl;

import org.example.backend.Entity.pojo.Conversation;
import org.example.backend.Entity.pojo.Message;
import org.example.backend.Entity.vo.MessageVO;
import org.example.backend.Service.AIService;
import org.example.backend.Mapper.MessageMapper;
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

    @Resource
    private MessageMapper messageMapper;
    
    // 摘要生成触发条件 - 消息数阈值
    private static final int SUMMARY_THRESHOLD = 10;

    @Override
    public List<MessageVO> getConversationMessages(Integer conversationId, Integer userId) {
    // 直接通过用户ID和会话ID验证用户是否有权限访问这个对话
        if (conversationService.getConversation(conversationId, userId) == null) {
        return new ArrayList<>(); // 对话不存在或不属于该用户
        }

    // 从数据库获取该对话的所有消息
    List<Message> messages = messageMapper.getConversationMessages(conversationId);
    
    // 转换为前端需要的VO对象
    List<MessageVO> result = new ArrayList<>();
    for (Message message : messages) {
        MessageVO vo = new MessageVO();
        vo.setId(message.getId().toString());
        vo.setRole(message.getRole());
        vo.setContent(message.getContent());
        vo.setTimestamp(message.getTimestamp());
        result.add(vo);
    }
    
    return result;
    }

    @Override
    @Transactional
    public MessageVO sendMessage(Integer conversationId, String content, Integer userId) {
        try {
        // 直接通过用户ID和会话ID验证用户是否有权限访问这个对话
            if (conversationService.getConversation(conversationId, userId) == null) {
            return null; // 对话不存在或不属于该用户
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
            
            // 摘要功能已禁用
            // checkAndUpdateSummary(conversationId, userId);

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
    // 保存消息到数据库
    messageMapper.saveMessage(message);
    return message.getId();
    }

    @Override
    public boolean deleteConversationMessages(Integer conversationId) {
    // 删除指定对话的所有消息
    int rows = messageMapper.deleteConversationMessages(conversationId);
    return rows >= 0; // 返回true，即使没有消息被删除
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
        // 摘要功能已禁用
        return false;
    }
} 