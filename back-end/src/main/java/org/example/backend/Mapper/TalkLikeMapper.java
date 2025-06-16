package org.example.backend.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TalkLikeMapper {
    @Insert("INSERT INTO rp_talk_like(userId, talkId, likeTime) VALUES (#{userId}, #{talkId}, CURRENT_TIMESTAMP)")
    int addNewLikeRecord(Integer userId, Integer talkId);

    @Select("SELECT id FROM rp_talk_like WHERE userId = #{userId} AND talkId = #{talkId}")
    Integer checkUserLikedTalk(Integer userId, Integer talkId);

    @Delete("DELETE FROM rp_talk_like WHERE userId = #{userId} AND talkId = #{talkId}")
    int deleteLikeRecord(Integer userId, Integer talkId);
}