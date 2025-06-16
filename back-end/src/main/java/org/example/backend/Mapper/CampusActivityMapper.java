package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.CampusActivity;

import java.util.List;

@Mapper
public interface CampusActivityMapper {
    
    @Insert("INSERT INTO campus_activity(activity_name, location, organizer, start_time, end_time, image, max_participants, current_participants, description, user_id, create_time, status) " +
            "VALUES(#{activityName}, #{location}, #{organizer}, #{startTime}, #{endTime}, #{image}, #{maxParticipants}, 0, #{description}, #{userId}, NOW(), 0)")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    Integer insertActivity(CampusActivity activity);
    
    @Select("SELECT id, activity_name as activityName, location, organizer, start_time as startTime, end_time as endTime, " +
            "image, max_participants as maxParticipants, current_participants as currentParticipants, " +
            "description, user_id as userId, create_time as createTime, status " +
            "FROM campus_activity WHERE id = #{id}")
    CampusActivity selectActivityById(Integer id);
    
    @Select("SELECT id, activity_name as activityName, location, organizer, start_time as startTime, end_time as endTime, " +
            "image, max_participants as maxParticipants, current_participants as currentParticipants, " +
            "description, user_id as userId, create_time as createTime, status " +
            "FROM campus_activity WHERE status = 1 ORDER BY create_time DESC")
    List<CampusActivity> selectAllApprovedActivities();
    
    @Select("SELECT id, activity_name as activityName, location, organizer, start_time as startTime, end_time as endTime, " +
            "image, max_participants as maxParticipants, current_participants as currentParticipants, " +
            "description, user_id as userId, create_time as createTime, status " +
            "FROM campus_activity WHERE status = #{status} ORDER BY create_time DESC")
    List<CampusActivity> selectActivitiesByStatus(Integer status);
    
    @Select("SELECT id, activity_name as activityName, location, organizer, start_time as startTime, end_time as endTime, " +
            "image, max_participants as maxParticipants, current_participants as currentParticipants, " +
            "description, user_id as userId, create_time as createTime, status " +
            "FROM campus_activity WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<CampusActivity> selectActivitiesByUserId(Integer userId);
    
    @Update("UPDATE campus_activity SET activity_name = #{activityName}, location = #{location}, organizer = #{organizer}, " +
            "start_time = #{startTime}, end_time = #{endTime}, image = #{image}, max_participants = #{maxParticipants}, " +
            "description = #{description} WHERE id = #{id}")
    Integer updateActivity(CampusActivity activity);
    
    @Update("UPDATE campus_activity SET status = #{status} WHERE id = #{id}")
    Integer updateActivityStatus(@Param("id") Integer id, @Param("status") Integer status);
    
    @Update("UPDATE campus_activity SET current_participants = current_participants + 1 WHERE id = #{id} AND current_participants < max_participants")
    Integer incrementParticipants(Integer id);
    
    @Update("UPDATE campus_activity SET current_participants = current_participants - 1 WHERE id = #{id} AND current_participants > 0")
    Integer decrementParticipants(Integer id);
    
    @Delete("DELETE FROM campus_activity WHERE id = #{id}")
    Integer deleteActivity(Integer id);
    
    @Update("UPDATE campus_activity SET status = 3 WHERE user_id = #{userId}")
    Integer markActivitiesAsUserDeleted(Integer userId);
} 