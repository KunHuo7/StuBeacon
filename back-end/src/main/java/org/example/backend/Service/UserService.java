package org.example.backend.Service;

import org.example.backend.Entity.pojo.NotificationQueryObject;
import org.example.backend.Entity.pojo.User;

import java.util.List;

public interface UserService {
    User login(String username, String password);
    User getUserById(int id);
    int getUnreadNotificationCount(int userId);
    List<NotificationQueryObject> getAllUsersNotificationByType(Integer userId,Integer type);
    String readNotificationById(Integer userId,int id);
    String clearNotificationByType(Integer userId,Integer type);
    User getUserByUsername(String username);
    boolean createUser(String username, String password, Integer manager);
    boolean createUserComplete(User user);
    String getAvatarById(Integer id);
    boolean updateAvatar(Integer id, String avatar);
    boolean updateManager(Integer id, Integer manager);
    
    // 管理员功能：获取所有用户
    List<User> getAllUsers();
    
    // 管理员功能：按用户名搜索用户
    List<User> searchUsersByUsername(String keyword);
    
    // 管理员功能：删除用户
    boolean deleteUser(Integer id);
    
    // 管理员功能：完整更新用户信息
    boolean updateUser(User user);
    
    // 管理员功能：更新用户密码
    boolean updatePassword(Integer id, String password);
    
    // 其他查询方法
    User getUserByMail(String mail);
    User getUserByStudentId(String studentId);
    
    // 检查用户是否是管理员
    boolean isUserAdmin(Integer userId);
    
    // =========================== 用户审核相关 ============================
    
    // 用户注册方法（创建待审核用户）
    boolean registerUser(String username, String password, String mail, String studentId);
    
    // 获取待审核用户列表
    List<User> getPendingUsers();
    
    // 审核通过用户
    boolean approveUser(Integer userId);
    
    // 拒绝用户注册
    boolean rejectUser(Integer userId);
    
    // 检查用户审核状态
    Integer getUserStatus(String username);
    
    // 通过用户名和密码获取已审核通过的用户（用于登录）
    User loginApprovedUser(String username, String password);
}
