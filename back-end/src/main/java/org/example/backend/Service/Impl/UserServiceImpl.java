package org.example.backend.Service.Impl;

import jakarta.annotation.Resource;
import org.example.backend.Entity.pojo.NotificationQueryObject;
import org.example.backend.Entity.pojo.User;
import org.example.backend.Mapper.NotificationMapper;
import org.example.backend.Mapper.UserMapper;
import org.example.backend.Mapper.CampusActivityMapper;
import org.example.backend.Service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service

public class UserServiceImpl implements UserService {
    @Resource
    UserMapper userMapper;
    @Resource
    NotificationMapper notificationMapper;
    @Resource
    CampusActivityMapper campusActivityMapper;
    @Override
    public User login(String username, String password) {
        return userMapper.getUserByUsernameAndPassword(username,password);
    }

    @Override
    public User getUserById(int id) {
        return userMapper.getUserById(id);
    }

    @Override
    public int getUnreadNotificationCount(int userId) {
        return notificationMapper.getUnreadNotificationCountByUserId(userId);
    }

    @Override
    public List<NotificationQueryObject> getAllUsersNotificationByType(Integer userId,Integer type) {
        return notificationMapper.getAllNotificationByUserIdAndType(userId,type);
    }



    @Override
    public String readNotificationById(Integer userId,int id) {
        if(notificationMapper.updateIsReadByRSId(id,1,userId)==1)return null; else return "出现错误，请稍后重试";
    }

    @Override
    public String clearNotificationByType(Integer userId, Integer type) {
        if(notificationMapper.updateIsReadByUserId(type,userId)>0) return null;else return "出现错误，请稍后重试";
    }

    @Override
    public User getUserByUsername(String username) {
        return userMapper.getUserByUsername(username);
    }

    @Override
    public boolean createUser(String username, String password, Integer manager) {
        return userMapper.createUser(username, password, manager);
    }
    
    @Override
    public boolean createUserComplete(User user) {
        return userMapper.createUserComplete(user) > 0;
    }

    @Override
    public String getAvatarById(Integer id) {
        return userMapper.getAvatarById(id);
    }

    @Override
    public boolean updateAvatar(Integer id, String avatar) {
        return userMapper.updateAvatar(id, avatar) > 0;
    }

    @Override
    public boolean updateManager(Integer id, Integer manager) {
        return userMapper.updateManager(id, manager) > 0;
    }
    
    @Override
    public List<User> getAllUsers() {
        return userMapper.getAllUsers();
    }
    
    @Override
    public List<User> searchUsersByUsername(String keyword) {
        return userMapper.searchUsersByUsername(keyword);
    }
    
    @Override
    @Transactional
    public boolean deleteUser(Integer id) {
        // 将用户发布的活动标记为状态3（用户已删除）
        campusActivityMapper.markActivitiesAsUserDeleted(id);
        
        // 删除用户
        return userMapper.deleteUser(id) > 0;
    }
    
    @Override
    public boolean updateUser(User user) {
        return userMapper.updateUser(user) > 0;
    }
    
    @Override
    public boolean updatePassword(Integer id, String password) {
        return userMapper.updatePassword(id, password) > 0;
    }
    
    @Override
    public User getUserByMail(String mail) {
        return userMapper.getUserByMail(mail);
    }
    
    @Override
    public User getUserByStudentId(String studentId) {
        return userMapper.getUserByStudentId(studentId);
    }
    
    @Override
    public boolean isUserAdmin(Integer userId) {
        User user = userMapper.getUserById(userId);
        return user != null && user.getManager() != null && user.getManager() == 1;
    }
    
    // =========================== 用户审核相关 ============================
    
    @Override
    public boolean registerUser(String username, String password, String mail, String studentId) {
        return userMapper.registerUser(username, password, mail, studentId) > 0;
    }
    
    @Override
    public List<User> getPendingUsers() {
        return userMapper.getPendingUsers();
    }
    
    @Override
    public boolean approveUser(Integer userId) {
        // 状态设为1表示审核通过
        return userMapper.updateUserStatus(userId, 1) > 0;
    }
    
    @Override
    public boolean rejectUser(Integer userId) {
        // 状态设为2表示审核拒绝
        return userMapper.updateUserStatus(userId, 2) > 0;
    }
    
    @Override
    public Integer getUserStatus(String username) {
        return userMapper.getUserStatus(username);
    }
    
    @Override
    public User loginApprovedUser(String username, String password) {
        return userMapper.getApprovedUserByUsernameAndPassword(username, password);
    }
}
