package org.example.backend.Service.Impl;

import org.example.backend.Entity.pojo.Conversation;
import org.example.backend.Entity.pojo.ConversationTag;
import org.example.backend.Entity.pojo.Message;
import org.example.backend.Entity.pojo.TagRelation;
import org.example.backend.Entity.vo.ConversationVO;
import org.example.backend.Entity.vo.MessageVO;
import org.example.backend.Service.AIService;
import org.example.backend.Service.ConversationService;
import org.example.backend.Service.MessageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 对话服务实现类
 */
@Service
public class ConversationServiceImpl implements ConversationService {
    
    @Resource
    private MessageService messageService;
    
    @Resource
    private AIService aiService;

    // TODO: 添加对话数据访问层依赖

    @Override
    public List<ConversationVO> getUserConversations(Integer userId) {
        // TODO: 从数据库获取对话列表并转换为VO
        // 临时返回空列表，等待数据访问层实现
        return new ArrayList<>();
    }

    @Override
    public Conversation getConversation(Integer conversationId, Integer userId) {
        // TODO: 实现从数据库获取对话并验证所有权
        // 临时返回null，等待数据访问层实现
        Conversation conversation = new Conversation();
        conversation.setId(conversationId);
        conversation.setUserId(userId);
        conversation.setTitle("临时对话");
        conversation.setCreatedAt(new Date());
        conversation.setUpdatedAt(new Date());
        conversation.setTotalMessages(0);
        return conversation;
    }

    @Override
    public Integer createConversation(String title, Integer userId) {
        // 创建新对话
        Conversation conversation = new Conversation(title, userId, new Date());
        
        // TODO: 保存到数据库
        // 临时返回一个模拟ID，等待数据访问层实现
        return 1; // 模拟ID
    }

    @Override
    public boolean updateConversation(Conversation conversation) {
        // TODO: 更新对话信息
        // 临时返回true，等待数据访问层实现
        return true;
    }

    @Override
    @Transactional
    public boolean deleteConversation(Integer conversationId, Integer userId) {
        try {
            // 验证用户是否有权限删除这个对话
            Conversation conversation = getConversation(conversationId, userId);
            if (conversation == null || !conversation.getUserId().equals(userId)) {
                return false;
            }
            
            // 先删除对话中的所有消息
            messageService.deleteConversationMessages(conversationId);
            
            // 再删除对话本身
            // TODO: 实现从数据库删除对话
            
            return true;
        } catch (Exception e) {
            // 处理异常，可以记录日志
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public String generateAndUpdateSummary(Integer conversationId, Integer userId) {
        try {
            // 验证用户是否有权限访问这个对话
            Conversation conversation = getConversation(conversationId, userId);
            if (conversation == null || !conversation.getUserId().equals(userId)) {
                return null;
            }
            
            // 获取对话中的所有消息
            List<MessageVO> messages = messageService.getConversationMessages(conversationId, userId);
            if (messages.isEmpty()) {
                return null;
            }
            
            // 构建要发送给AI的提示
            StringBuilder prompt = new StringBuilder();
            prompt.append("请根据以下对话内容，生成一个简短的摘要（不超过50个字），该摘要应该能反映对话的主题和内容。\n\n");
            
            // 添加不超过最近10条消息，避免过长
            int startIndex = Math.max(0, messages.size() - 10);
            for (int i = startIndex; i < messages.size(); i++) {
                MessageVO msg = messages.get(i);
                prompt.append(msg.getRole()).append(": ").append(msg.getContent()).append("\n");
            }
            
            // 调用AI服务生成摘要
            String summary = aiService.chat(userId.toString(), prompt.toString());
            
            // 处理摘要，确保不超过限制
            if (summary != null && summary.length() > 500) {
                summary = summary.substring(0, 500);
            }
            
            // 更新对话摘要
            conversation.setSummary(summary);
            updateConversation(conversation);
            
            return summary;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    @Transactional
    public boolean addTagToConversation(Integer conversationId, String tagName, Integer userId) {
        try {
            // 验证用户是否有权限访问这个对话
            Conversation conversation = getConversation(conversationId, userId);
            if (conversation == null || !conversation.getUserId().equals(userId)) {
                return false;
            }
            
            // 检查标签是否已存在
            ConversationTag tag = getOrCreateTag(tagName, userId);
            if (tag == null || tag.getId() == null) {
                return false;
            }
            
            // 检查关联是否已存在
            // TODO: 检查标签关联是否已存在
            
            // 创建标签关联
            TagRelation relation = new TagRelation(conversationId, tag.getId());
            // TODO: 保存标签关联
            
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public List<ConversationTag> getUserTags(Integer userId) {
        // TODO: 从数据库获取用户的所有标签
        return new ArrayList<>();
    }
    
    @Override
    public List<ConversationVO> getConversationsByTag(Integer tagId, Integer userId) {
        // TODO: 从数据库获取指定标签的对话
        return new ArrayList<>();
    }
    
    /**
     * 获取或创建标签
     * 
     * @param tagName 标签名称
     * @param userId 用户ID
     * @return 标签对象
     */
    private ConversationTag getOrCreateTag(String tagName, Integer userId) {
        // TODO: 实现从数据库获取标签或创建新标签
        ConversationTag tag = new ConversationTag(tagName, userId);
        tag.setId(1); // 模拟ID
        return tag;
    }
} 