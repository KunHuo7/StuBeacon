package org.example.backend.Service;

import org.example.backend.Entity.pojo.Conversation;
import org.example.backend.Entity.pojo.ConversationTag;
import org.example.backend.Entity.vo.ConversationVO;

import java.util.List;

/**
 * AI对话服务接口
 */
public interface ConversationService {
    
    /**
     * 获取用户的所有对话
     * 
     * @param userId 用户ID
     * @return 对话列表
     */
    List<ConversationVO> getUserConversations(Integer userId);
    
    /**
     * 获取特定对话详情
     * 
     * @param conversationId 对话ID
     * @param userId 用户ID（用于验证权限）
     * @return 对话详情，如果不存在或无权限返回null
     */
    Conversation getConversation(Integer conversationId, Integer userId);
    
    /**
     * 创建新对话
     * 
     * @param title 对话标题
     * @param userId 用户ID
     * @return 新创建的对话ID
     */
    Integer createConversation(String title, Integer userId);
    
    /**
     * 更新对话标题或最后消息
     * 
     * @param conversation 更新后的对话
     * @return 是否更新成功
     */
    boolean updateConversation(Conversation conversation);
    
    /**
     * 删除对话
     * 
     * @param conversationId 对话ID
     * @param userId 用户ID（用于验证权限）
     * @return 是否删除成功
     */
    boolean deleteConversation(Integer conversationId, Integer userId);
    
    /**
     * 生成并更新对话摘要
     * 
     * @param conversationId 对话ID
     * @param userId 用户ID（用于验证权限）
     * @return 生成的摘要
     */
    String generateAndUpdateSummary(Integer conversationId, Integer userId);
    
    /**
     * 为对话添加标签
     * 
     * @param conversationId 对话ID
     * @param tagName 标签名称
     * @param userId 用户ID（用于验证权限）
     * @return 是否成功
     */
    boolean addTagToConversation(Integer conversationId, String tagName, Integer userId);
    
    /**
     * 获取用户的所有标签
     * 
     * @param userId 用户ID
     * @return 标签列表
     */
    List<ConversationTag> getUserTags(Integer userId);
    
    /**
     * 按标签查询对话
     * 
     * @param tagId 标签ID
     * @param userId 用户ID（用于验证权限）
     * @return 对话列表
     */
    List<ConversationVO> getConversationsByTag(Integer tagId, Integer userId);
} 