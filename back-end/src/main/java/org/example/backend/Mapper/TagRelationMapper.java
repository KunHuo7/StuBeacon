package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.TagRelation;

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
    int createRelation(TagRelation relation);
    
    /**
     * 查询特定关联
     */
    @Select("SELECT * FROM tb_conversation_tag_relations WHERE conversation_id = #{conversationId} AND tag_id = #{tagId}")
    TagRelation getRelation(@Param("conversationId") Integer conversationId, @Param("tagId") Integer tagId);
    
    /**
     * 删除关联
     */
    @Delete("DELETE FROM tb_conversation_tag_relations WHERE id = #{id}")
    int deleteRelation(Integer id);
    
    /**
     * 删除对话的所有标签关联
     */
    @Delete("DELETE FROM tb_conversation_tag_relations WHERE conversation_id = #{conversationId}")
    int deleteConversationRelations(Integer conversationId);
    
    /**
     * 删除标签的所有关联
     */
    @Delete("DELETE FROM tb_conversation_tag_relations WHERE tag_id = #{tagId}")
    int deleteTagRelations(Integer tagId);
}