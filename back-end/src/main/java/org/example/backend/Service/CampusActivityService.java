package org.example.backend.Service;

import org.example.backend.Entity.pojo.CampusActivity;
import org.example.backend.Entity.pojo.User;
import org.example.backend.Entity.requestbodies.ActivityRequest;

import java.util.List;

public interface CampusActivityService {
    
    // 发布活动
    Integer publishActivity(ActivityRequest request, Integer userId);
    
    // 获取活动信息
    CampusActivity getActivityById(Integer id);
    
    // 获取所有已批准的活动
    List<CampusActivity> getAllApprovedActivities();
    
    // 获取待审核的活动
    List<CampusActivity> getPendingActivities();
    
    // 获取用户发布的活动
    List<CampusActivity> getUserActivities(Integer userId);
    
    // 更新活动信息
    boolean updateActivity(ActivityRequest request, Integer userId);
    
    // 审核活动
    boolean reviewActivity(Integer id, Integer status);
    
    // 删除活动
    boolean deleteActivity(Integer id, Integer userId);
    
    // 参加活动
    boolean joinActivity(Integer activityId, Integer userId);
    
    // 退出活动
    boolean quitActivity(Integer activityId, Integer userId);
    
    // 检查用户是否已参加活动
    boolean checkUserJoined(Integer activityId, Integer userId);
    
    // 获取用户参加的所有活动
    List<CampusActivity> getUserJoinedActivities(Integer userId);
    
    // 获取活动参与者列表
    List<User> getActivityParticipants(Integer activityId);
} 