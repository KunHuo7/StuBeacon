package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.Conversation;
import org.example.backend.Entity.vo.ConversationVO;

import java.util.List;

/**
 * 对话数据访问接口
 */
@Mapper
public interface ConversationMapper {
    
    /**
     * 获取用户的所有对话
     */
    @Select("SELECT * FROM tb_conversations WHERE user_id = #{userId} ORDER BY updated_at DESC")
    List<Conversation> getUserConversations(Integer userId);
    
    /**
     * 获取特定对话
     */
    @Select("SELECT * FROM tb_conversations WHERE id = #{id}")
    Conversation getConversationById(Integer id);
    
    /**
     * 创建新对话
     */
    @Insert("INSERT INTO tb_conversations(title, user_id, created_at, updated_at, total_messages) " +
            "VALUES(#{title}, #{userId}, #{createdAt}, #{updatedAt}, #{totalMessages})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createConversation(Conversation conversation);
    
    /**
     * 更新对话信息
     */
    @Update("UPDATE tb_conversations SET title = #{title}, summary = #{summary}, " +
            "updated_at = #{updatedAt}, last_message = #{lastMessage}, " +
            "total_messages = #{totalMessages} WHERE id = #{id}")
    int updateConversation(Conversation conversation);
    
    /**
     * 删除对话
     */
    @Delete("DELETE FROM tb_conversations WHERE id = #{id}")
    int deleteConversation(Integer id);
    
    /**
     * 根据标签ID获取对话
     */
    @Select("SELECT c.* FROM tb_conversations c " +
            "JOIN tb_conversation_tag_relations r ON c.id = r.conversation_id " +
            "WHERE r.tag_id = #{tagId} AND c.user_id = #{userId} " +
            "ORDER BY c.updated_at DESC")
    List<Conversation> getConversationsByTag(@Param("tagId") Integer tagId, @Param("userId") Integer userId);
    
    /**
     * 获取对话的标签名称列表
     */
    @Select("SELECT t.name FROM tb_conversation_tags t " +
            "JOIN tb_conversation_tag_relations r ON t.id = r.tag_id " +
            "WHERE r.conversation_id = #{conversationId}")
    List<String> getConversationTags(Integer conversationId);
}