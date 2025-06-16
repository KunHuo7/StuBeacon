package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.ActivityParticipant;

import java.util.List;

@Mapper
public interface ActivityParticipantMapper {
    
    @Insert("INSERT INTO activity_participant(activity_id, user_id, join_time) VALUES(#{activityId}, #{userId}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertParticipant(ActivityParticipant participant);
    
    @Select("SELECT id, activity_id as activityId, user_id as userId, join_time as joinTime FROM activity_participant WHERE activity_id = #{activityId}")
    List<ActivityParticipant> selectParticipantsByActivityId(Integer activityId);
    
    @Select("SELECT id, activity_id as activityId, user_id as userId, join_time as joinTime FROM activity_participant WHERE user_id = #{userId}")
    List<ActivityParticipant> selectParticipantsByUserId(Integer userId);
    
    @Select("SELECT id, activity_id as activityId, user_id as userId, join_time as joinTime FROM activity_participant WHERE activity_id = #{activityId} AND user_id = #{userId}")
    ActivityParticipant selectParticipantByActivityAndUser(@Param("activityId") Integer activityId, @Param("userId") Integer userId);
    
    @Delete("DELETE FROM activity_participant WHERE activity_id = #{activityId} AND user_id = #{userId}")
    Integer deleteParticipant(@Param("activityId") Integer activityId, @Param("userId") Integer userId);
    
    @Delete("DELETE FROM activity_participant WHERE activity_id = #{activityId}")
    Integer deleteAllParticipantsByActivityId(Integer activityId);
} 