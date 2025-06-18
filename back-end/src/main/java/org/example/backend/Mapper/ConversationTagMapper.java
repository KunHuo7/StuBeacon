package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.ConversationTag;

import java.util.List;

/**
 * 对话标签数据访问接口
 */
@Mapper
public interface ConversationTagMapper {
    
    /**
     * 获取用户的所有标签
     */
    @Select("SELECT * FROM tb_conversation_tags WHERE user_id = #{userId}")
    List<ConversationTag> getUserTags(Integer userId);
    
    /**
     * 根据标签名称和用户ID获取标签
     */
    @Select("SELECT * FROM tb_conversation_tags WHERE name = #{name} AND user_id = #{userId}")
    ConversationTag getTagByNameAndUserId(@Param("name") String name, @Param("userId") Integer userId);
    
    /**
     * 创建新标签
     */
    @Insert("INSERT INTO tb_conversation_tags(name, user_id, created_at) VALUES(#{name}, #{userId}, #{createdAt})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int createTag(ConversationTag tag);
    
    /**
     * 删除标签
     */
    @Delete("DELETE FROM tb_conversation_tags WHERE id = #{id}")
    int deleteTag(Integer id);
    
    /**
     * 根据ID获取标签
     */
    @Select("SELECT * FROM tb_conversation_tags WHERE id = #{id}")
    ConversationTag getTagById(Integer id);
}