package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.TagRelation;

import java.util.List;

/**
 * 标签关联数据访问接口
 */
@Mapper
public interface TagRelationMapper {
    
    /**
     * 创建标签关联
     */
    @Insert("INSERT INTO tb_conversation_tag_relations(conversation_id, tag_id) VALUES(#{conversationId}, #{tagId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createTagRelation(TagRelation relation);
    
    /**
     * 检查标签关联是否存在
     */
    @Select("SELECT COUNT(*) FROM tb_conversation_tag_relations WHERE conversation_id = #{conversationId} AND tag_id = #{tagId}")
    int checkTagRelationExists(@Param("conversationId") Integer conversationId, @Param("tagId") Integer tagId);
    
    /**
     * 获取对话的所有标签ID
     */
    @Select("SELECT tag_id FROM tb_conversation_tag_relations WHERE conversation_id = #{conversationId}")
    List<Integer> getConversationTagIds(Integer conversationId);
    
    /**
     * 删除标签关联
     */
    @Delete("DELETE FROM tb_conversation_tag_relations WHERE conversation_id = #{conversationId} AND tag_id = #{tagId}")
    int deleteTagRelation(@Param("conversationId") Integer conversationId, @Param("tagId") Integer tagId);
    
    /**
     * 删除对话的所有标签关联
     */
    @Delete("DELETE FROM tb_conversation_tag_relations WHERE conversation_id = #{conversationId}")
    int deleteConversationTagRelations(Integer conversationId);
}