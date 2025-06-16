package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.Talk;
import org.example.backend.util.JsonArrayTypeHandler;

import java.util.List;

@Mapper
public interface TalkMapper {
    @Insert("INSERT INTO tb_talks(title, authorId, createTime, content, tags, views, likes, comments) VALUES (#{title}, #{authorId}, #{createTime}, #{content}, #{tags, typeHandler=org.example.backend.util.JsonArrayTypeHandler}, #{views}, #{likes}, #{comments})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertNewTalk(Talk talk);

    
    @Select("SELECT id, title, substring(content, 1, 40) AS content, authorId, views, likes, comments, createTime, tags FROM tb_talks ORDER BY id DESC LIMIT 50")
    @Results({
        @Result(property = "tags", column = "tags", typeHandler = JsonArrayTypeHandler.class)
    })
    List<Talk> getTalksOrderById();
    
    @Select("SELECT * FROM tb_talks WHERE id = #{id}")
    @Results({
        @Result(property = "tags", column = "tags", typeHandler = JsonArrayTypeHandler.class)
    })
    Talk getTalkById(int id);
    
    @Update("UPDATE tb_talks SET views = views + 1 WHERE id = #{id}")
    int updateViewsById(int id);
    
    @Update("UPDATE tb_talks SET likes = likes + 1 WHERE id = #{id}")
    int addLikesById(int id);
    
    @Update("UPDATE tb_talks SET likes = likes - 1 WHERE id = #{id}")
    int unlikeById(int id);
    
    @Update("UPDATE tb_talks SET comments = comments + 1 WHERE id = #{id}")
    int incrementCommentsById(int id);
    
    @Update("UPDATE tb_talks SET comments = comments - 1 WHERE id = #{id}")
    int decrementCommentsById(int id);
    
    @Select("SELECT * FROM tb_talks WHERE JSON_CONTAINS(tags, CONCAT('\"', #{tag}, '\"'))")
    @Results({
        @Result(property = "tags", column = "tags", typeHandler = JsonArrayTypeHandler.class)
    })
    List<Talk> getTalksByTag(String tag);
    
    @Delete("DELETE FROM tb_talks WHERE id = #{id}")
    int deleteTalkById(int id);
}