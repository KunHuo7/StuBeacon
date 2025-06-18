package org.example.backend.Service;

import org.example.backend.Entity.pojo.Message;
import org.example.backend.Entity.vo.MessageVO;
import java.util.List;

/**
 * AI对话消息服务接口
 */
public interface MessageService {
    
    /**
     * 获取对话中的所有消息
     * 
     * @param conversationId 对话ID
     * @param userId 用户ID（用于验证权限）
     * @return 消息列表
     */
    List<MessageVO> getConversationMessages(Integer conversationId, Integer userId);
    
    /**
     * 添加消息并获取AI回答
     * 
     * @param conversationId 对话ID
     * @param content 用户消息内容
     * @param userId 用户ID
     * @return AI回复消息
     */
    MessageVO sendMessage(Integer conversationId, String content, Integer userId);
    
    /**
     * 保存消息
     * 
     * @param message 消息对象
     * @return 消息ID
     */
    Integer saveMessage(Message message);
    
    /**
     * 删除特定对话的所有消息
     * 
     * @param conversationId 对话ID
     * @return 是否删除成功
     */
    boolean deleteConversationMessages(Integer conversationId);
    
    /**
     * 根据最新消息更新对话状态
     * 包括最后消息预览、消息数量统计等
     * 
     * @param conversationId 对话ID
     * @param message 最新消息
     * @return 是否更新成功
     */
    boolean updateConversationStatus(Integer conversationId, Message message);
    
    /**
     * 检查对话是否需要生成摘要
     * 当消息数达到一定数量（如5条、10条）时，可以触发摘要生成
     * 
     * @param conversationId 对话ID
     * @param userId 用户ID
     * @return 是否需要生成摘要
     */
    boolean checkAndUpdateSummary(Integer conversationId, Integer userId);
} 