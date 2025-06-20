package org.example.backend.Service.Impl;

import org.example.backend.Entity.pojo.Conversation;
import org.example.backend.Entity.pojo.ConversationTag;
import org.example.backend.Entity.pojo.Message;
import org.example.backend.Entity.pojo.TagRelation;
import org.example.backend.Entity.vo.ConversationVO;
import org.example.backend.Entity.vo.MessageVO;
import org.example.backend.Mapper.ConversationMapper;
import org.example.backend.Mapper.ConversationTagMapper;
import org.example.backend.Mapper.TagRelationMapper;
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

    @Resource
    private ConversationMapper conversationMapper;
    
    @Resource
    private ConversationTagMapper conversationTagMapper;
    
    @Resource
    private TagRelationMapper tagRelationMapper;

    @Override
    public List<ConversationVO> getUserConversations(Integer userId) {
    // 获取用户的所有对话
    List<Conversation> conversations = conversationMapper.getUserConversations(userId);
    
    // 转换为 VO 对象列表
    List<ConversationVO> result = new ArrayList<>();
    for (Conversation conversation : conversations) {
        ConversationVO vo = new ConversationVO();
        vo.setId(conversation.getId().toString());
        vo.setTitle(conversation.getTitle());
        // 摘要功能已禁用
        vo.setSummary(null);
        vo.setLastMessage(conversation.getLastMessage());
        
        // 确保日期不为null
        vo.setDate(conversation.getUpdatedAt() != null ? conversation.getUpdatedAt() : 
                  (conversation.getCreatedAt() != null ? conversation.getCreatedAt() : new Date()));
        
        vo.setTotalMessages(conversation.getTotalMessages());
        
        // 标签功能已禁用
        vo.setTags(new ArrayList<>());
        
        result.add(vo);
    }
    
    return result;
    }

    @Override
    public Conversation getConversation(Integer conversationId, Integer userId) {
    // 直接通过用户ID和会话ID获取对话，不做额外权限验证
    // 如果会话属于该用户，则返回；否则返回null
    if (userId != null) {
        return conversationMapper.getConversationByIdAndUserId(conversationId, userId);
    } else {
        // 如果没有提供用户ID，则只根据会话ID获取
        return conversationMapper.getConversationById(conversationId);
    }
    }

    @Override
    public Integer createConversation(String title, Integer userId) {
        // 创建新对话
        Conversation conversation = new Conversation(title, userId, new Date());
        
    // 保存到数据库
    conversationMapper.createConversation(conversation);
    
    return conversation.getId();
    }

    @Override
    public boolean updateConversation(Conversation conversation) {
    // 更新对话信息
    int rows = conversationMapper.updateConversation(conversation);
    return rows > 0;
    }

    @Override
    @Transactional
    public boolean deleteConversation(Integer conversationId, Integer userId) {
        try {
        // 使用简化的方法验证对话是否属于该用户
            Conversation conversation = getConversation(conversationId, userId);
        if (conversation == null) {
            return false; // 对话不存在或不属于该用户
            }
            
            // 先删除对话中的所有消息
            messageService.deleteConversationMessages(conversationId);
            
            // 再删除对话本身
        int rows = conversationMapper.deleteConversation(conversationId);
            
        return rows > 0;
        } catch (Exception e) {
            // 处理异常，可以记录日志
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public String generateAndUpdateSummary(Integer conversationId, Integer userId) {
        // 摘要功能已禁用
            return null;
    }
    
    @Override
    @Transactional
    public boolean addTagToConversation(Integer conversationId, String tagName, Integer userId) {
    // 标签功能已禁用
                return false;
    }
    
    @Override
    public List<ConversationTag> getUserTags(Integer userId) {
    // 标签功能已禁用
        return new ArrayList<>();
    }
    
    @Override
    public List<ConversationVO> getConversationsByTag(Integer tagId, Integer userId) {
    // 标签功能已禁用
        return new ArrayList<>();
    }
    
    /**
 * 获取或创建标签 (已禁用)
     * 
     * @param tagName 标签名称
     * @param userId 用户ID
     * @return 标签对象
     */
    private ConversationTag getOrCreateTag(String tagName, Integer userId) {
    // 标签功能已禁用
    return null;
    }
} 