package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.TalkComment;

import java.util.List;

@Mapper
public interface TalkCommentMapper {
    @Insert("INSERT INTO tb_talk_comments(authorId, talkId, content, createTime, parentId) VALUES (#{authorId}, #{talkId}, #{content}, #{createTime}, #{parentId})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertNewComment(TalkComment comment);
    
    @Select("SELECT * FROM tb_talk_comments WHERE talkId = #{talkId} ORDER BY createTime DESC")
    List<TalkComment> getCommentsByTalkId(Integer talkId);
    
    @Update("UPDATE tb_talk_comments SET likes = likes + 1 WHERE id = #{id}")
    int incrementLikesById(Integer id);
    
    @Update("UPDATE tb_talk_comments SET likes = likes - 1 WHERE id = #{id}")
    int decrementLikesById(Integer id);
    
    @Delete("DELETE FROM tb_talk_comments WHERE id = #{id}")
    int deleteCommentById(Integer id);
    
    @Select("SELECT COUNT(*) FROM tb_talk_comments WHERE talkId = #{talkId}")
    int countCommentsByTalkId(Integer talkId);
    
    @Select("SELECT * FROM tb_talk_comments WHERE id = #{id}")
    TalkComment getCommentById(Integer id);
    
    @Select("SELECT * FROM tb_talk_comments WHERE parentId = #{parentId} ORDER BY createTime ASC")
    List<TalkComment> getRepliesByParentId(Integer parentId);
}