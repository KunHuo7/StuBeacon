package org.example.backend.Service.Impl;

import jakarta.annotation.Resource;
import org.example.backend.Entity.pojo.ActivityParticipant;
import org.example.backend.Entity.pojo.CampusActivity;
import org.example.backend.Entity.pojo.User;
import org.example.backend.Entity.requestbodies.ActivityRequest;
import org.example.backend.Mapper.ActivityParticipantMapper;
import org.example.backend.Mapper.CampusActivityMapper;
import org.example.backend.Mapper.UserMapper;
import org.example.backend.Service.CampusActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CampusActivityServiceImpl implements CampusActivityService {

    @Resource
    private CampusActivityMapper activityMapper;
    
    @Resource
    private ActivityParticipantMapper participantMapper;
    
    @Resource
    private UserMapper userMapper;

    @Override
    public Integer publishActivity(ActivityRequest request, Integer userId) {
        // 创建活动并保存
        CampusActivity activity = new CampusActivity();
        activity.setActivityName(request.getActivityName());
        activity.setLocation(request.getLocation());
        activity.setOrganizer(request.getOrganizer());
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());
        activity.setImage(request.getImage());
        activity.setMaxParticipants(request.getMaxParticipants());
        activity.setCurrentParticipants(0);
        activity.setDescription(request.getDescription());
        activity.setUserId(userId);
        activity.setStatus(0); // 默认待审核
        
        activityMapper.insertActivity(activity);
        return activity.getId();
    }

    @Override
    public CampusActivity getActivityById(Integer id) {
        return activityMapper.selectActivityById(id);
    }

    @Override
    public List<CampusActivity> getAllApprovedActivities() {
        List<CampusActivity> approvedActivities = activityMapper.selectAllApprovedActivities();
        // 只返回用户存在的活动（数据库查询已排除状态为3的活动）
        return approvedActivities.stream()
                .filter(activity -> {
                    if (activity.getUserId() == null) {
                        return false;
                    }
                    User user = userMapper.getUserById(activity.getUserId());
                    return user != null; // 只返回用户存在的活动
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CampusActivity> getPendingActivities() {
        List<CampusActivity> pendingActivities = activityMapper.selectActivitiesByStatus(0);
        // 过滤掉被删除用户的活动
        return pendingActivities.stream()
                .filter(activity -> {
                    if (activity.getUserId() == null) {
                        return false;
                    }
                    User user = userMapper.getUserById(activity.getUserId());
                    return user != null; // 只返回用户存在的活动
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<CampusActivity> getUserActivities(Integer userId) {
        // 检查用户是否存在
        User user = userMapper.getUserById(userId);
        if (user == null) {
            return new ArrayList<>(); // 如果用户不存在，返回空列表
        }
        return activityMapper.selectActivitiesByUserId(userId);
    }

    @Override
    public boolean updateActivity(ActivityRequest request, Integer userId) {
        // 获取活动信息
        CampusActivity existingActivity = activityMapper.selectActivityById(request.getId());
        if (existingActivity == null) {
            return false;
        }
        
        // 检查权限（只有发布者或管理员可以编辑）
        User user = userMapper.getUserById(userId);
        if (!existingActivity.getUserId().equals(userId) && (user.getManager() == null || user.getManager() != 1)) {
            return false;
        }
        
        // 更新活动
        CampusActivity activity = new CampusActivity();
        activity.setId(request.getId());
        activity.setActivityName(request.getActivityName());
        activity.setLocation(request.getLocation());
        activity.setOrganizer(request.getOrganizer());
        activity.setStartTime(request.getStartTime());
        activity.setEndTime(request.getEndTime());
        activity.setImage(request.getImage());
        activity.setMaxParticipants(request.getMaxParticipants());
        activity.setDescription(request.getDescription());
        
        return activityMapper.updateActivity(activity) > 0;
    }

    @Override
    public boolean reviewActivity(Integer id, Integer status) {
        return activityMapper.updateActivityStatus(id, status) > 0;
    }

    @Override
    @Transactional
    public boolean deleteActivity(Integer id, Integer userId) {
        // 获取活动信息
        CampusActivity activity = activityMapper.selectActivityById(id);
        if (activity == null) {
            return false;
        }
        
        // 检查权限（只有发布者或管理员可以删除）
        User user = userMapper.getUserById(userId);
        if (activity.getUserId() == null || (!activity.getUserId().equals(userId) && (user.getManager() == null || user.getManager() != 1))) {
            return false;
        }
        
        // 删除所有参与记录
        participantMapper.deleteAllParticipantsByActivityId(id);
        
        // 删除活动
        return activityMapper.deleteActivity(id) > 0;
    }

    @Override
    @Transactional
    public boolean joinActivity(Integer activityId, Integer userId) {
        // 检查活动是否存在且已审核通过
        CampusActivity activity = activityMapper.selectActivityById(activityId);
        if (activity == null || activity.getStatus() != 1) {
            return false;
        }
        
        // 检查用户是否已参加
        if (checkUserJoined(activityId, userId)) {
            return false;
        }
        
        // 检查是否已满
        if (activity.getCurrentParticipants() >= activity.getMaxParticipants()) {
            return false;
        }
        
        // 添加参与记录
        ActivityParticipant participant = new ActivityParticipant();
        participant.setActivityId(activityId);
        participant.setUserId(userId);
        participantMapper.insertParticipant(participant);
        
        // 更新活动参与人数
        return activityMapper.incrementParticipants(activityId) > 0;
    }

    @Override
    @Transactional
    public boolean quitActivity(Integer activityId, Integer userId) {
        // 检查用户是否已参加
        if (!checkUserJoined(activityId, userId)) {
            return false;
        }
        
        // 删除参与记录
        participantMapper.deleteParticipant(activityId, userId);
        
        // 更新活动参与人数
        return activityMapper.decrementParticipants(activityId) > 0;
    }

    @Override
    public boolean checkUserJoined(Integer activityId, Integer userId) {
        return participantMapper.selectParticipantByActivityAndUser(activityId, userId) != null;
    }

    @Override
    public List<CampusActivity> getUserJoinedActivities(Integer userId) {
//        System.out.println("正在获取用户ID为" + userId + "参加的活动列表");
        
        // 获取用户参加的所有活动ID
        List<ActivityParticipant> participants = participantMapper.selectParticipantsByUserId(userId);
        
        if (participants.isEmpty()) {
//            System.out.println("用户未参加任何活动");
            return new ArrayList<>();
        }
        
//        System.out.println("用户参加的活动数量: " + participants.size());
        
        // 收集所有活动ID
        List<Integer> activityIds = new ArrayList<>();
        for (ActivityParticipant participant : participants) {
            activityIds.add(participant.getActivityId());
//            System.out.println("用户参加的活动ID: " + participant.getActivityId() + ", 参加时间: " + participant.getJoinTime());
        }
        
        // 查询所有活动的详细信息
        List<CampusActivity> activities = new ArrayList<>();
        for (Integer activityId : activityIds) {
            CampusActivity activity = activityMapper.selectActivityById(activityId);
            if (activity != null) {
                activities.add(activity);
//                System.out.println("已获取活动: " + activity.getId() + ", 名称: " + activity.getActivityName());
            } else {
//                System.out.println("警告：活动ID " + activityId + " 在数据库中不存在");
            }
        }
        
//        System.out.println("最终返回活动数量: " + activities.size());
        return activities;
    }
    
    @Override
    public List<User> getActivityParticipants(Integer activityId) {
//        System.out.println("正在获取活动ID为" + activityId + "的参与者列表");
        
        // 获取活动的所有参与者ID
        List<ActivityParticipant> participants = participantMapper.selectParticipantsByActivityId(activityId);
        
        if (participants.isEmpty()) {
//            System.out.println("该活动没有参与者");
            return new ArrayList<>();
        }
        
//        System.out.println("活动参与者数量: " + participants.size());
        
        // 查询所有参与者的用户信息
        List<User> users = new ArrayList<>();
        for (ActivityParticipant participant : participants) {
            User user = userMapper.getUserById(participant.getUserId());
            if (user != null) {
                // 移除敏感信息
                user.setPassword(null);
                users.add(user);
//                System.out.println("已获取参与者: " + user.getId() + ", 用户名: " + user.getUsername());
            } else {
//                System.out.println("警告：用户ID " + participant.getUserId() + " 在数据库中不存在");
            }
        }
        
//        System.out.println("最终返回参与者数量: " + users.size());
        return users;
    }
} 
