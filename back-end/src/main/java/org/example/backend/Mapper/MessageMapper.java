package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.Message;

import java.util.List;

/**
 * 消息数据访问接口
 */
@Mapper
public interface MessageMapper {
    
    /**
     * 获取对话中的所有消息
     */
    @Select("SELECT * FROM tb_messages WHERE conversation_id = #{conversationId} ORDER BY timestamp ASC")
    List<Message> getConversationMessages(Integer conversationId);
    
    /**
     * 保存新消息
     */
    @Insert("INSERT INTO tb_messages(conversation_id, role, content, timestamp) " +
            "VALUES(#{conversationId}, #{role}, #{content}, #{timestamp})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int saveMessage(Message message);
    
    /**
     * 获取特定消息
     */
    @Select("SELECT * FROM tb_messages WHERE id = #{id}")
    Message getMessageById(Integer id);
    
    /**
     * 删除特定对话的所有消息
     */
    @Delete("DELETE FROM tb_messages WHERE conversation_id = #{conversationId}")
    int deleteConversationMessages(Integer conversationId);
    
    /**
     * 统计对话中的消息数量
     */
    @Select("SELECT COUNT(*) FROM tb_messages WHERE conversation_id = #{conversationId}")
    int countConversationMessages(Integer conversationId);
    
    /**
     * 获取对话中最近的N条消息
     */
    @Select("SELECT * FROM tb_messages WHERE conversation_id = #{conversationId} " +
            "ORDER BY timestamp DESC LIMIT #{limit}")
    List<Message> getRecentMessages(@Param("conversationId") Integer conversationId, @Param("limit") Integer limit);
}