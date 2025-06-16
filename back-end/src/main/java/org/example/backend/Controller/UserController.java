package org.example.backend.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.backend.Entity.pojo.NotificationQueryObject;
import org.example.backend.Entity.pojo.RestBean;
import org.example.backend.Entity.pojo.User;
import org.example.backend.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")

public class UserController {
    @Resource
    UserService userService;
    @GetMapping("myinfo")
    public RestBean<User> getMyinfo(HttpServletRequest request){
        User user=userService.getUserById((Integer)request.getAttribute("id"));
        user.setPassword(null);
        user.setUnReadNotiCnt(userService.getUnreadNotificationCount(user.getId()));
        return RestBean.success("成功",user);
    }
    @GetMapping("logout")
    public RestBean<String> logout(HttpSession session){
        User logoinUser=(User) session.getAttribute("user");
        if(logoinUser==null)return RestBean.failure(401,"您尚未登入");
        else{
            session.removeAttribute("user");
            session.invalidate();
        }
        return RestBean.success("登出成功");
    }

    @GetMapping("getNotifications")
    public RestBean<List<NotificationQueryObject>> getAllUsersNotifications(@RequestParam("type")Integer type,
                                                                            HttpServletRequest request)
    {
        Integer userId=(Integer) request.getAttribute("id");
        return RestBean.success("请求成功",userService.getAllUsersNotificationByType(userId,type));
    }

    @PostMapping("readNotification")
    public RestBean<String> readNotification(@RequestParam("id")Integer id,HttpServletRequest request){
        Integer userId=(Integer) request.getAttribute("id");
        String status=userService.readNotificationById(userId,id);
        if (status!=null) return RestBean.failure(501,status);else return RestBean.success("已读成功");
    }

    @PostMapping("clearNotification")
    public RestBean<String> clearNotification(@RequestParam("type")Integer type,HttpServletRequest request){
        Integer userId=(Integer) request.getAttribute("id");
        String status=userService.clearNotificationByType(userId,type);
        if (status!=null) return RestBean.failure(501,status);else return RestBean.success("已读成功");
    }

    @PostMapping("/updateAvatar")
    public RestBean<String> updateAvatar(@RequestBody User user) {
        if (userService.updateAvatar(user.getId(), user.getAvatar())) {
            return RestBean.success("头像更新成功");
        }
        return RestBean.failure(500, "头像更新失败");
    }

    @PostMapping("/updateManager")
    public RestBean<String> updateManager(@RequestParam Integer userId, @RequestParam Integer manager) {
        // 验证当前操作用户是否为管理员 (此处可添加权限检查)
        if (userService.updateManager(userId, manager)) {
            return RestBean.success("用户权限更新成功");
        }
        return RestBean.failure(500, "用户权限更新失败");
    }
    
    // =================== 管理员功能 ===================
    
    /**
     * 检查当前用户是否为管理员
     */
    private RestBean<String> checkAdminPermission(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("id");
        if (!userService.isUserAdmin(userId)) {
            return RestBean.failure(403, "权限不足，仅管理员可执行此操作");
        }
        return null;
    }
    
    /**
     * 获取所有用户列表（管理员专用）
     */
    @GetMapping("/admin/list")
    public RestBean<?> getAllUsers(HttpServletRequest request) {
        RestBean<String> permissionCheck = checkAdminPermission(request);
        if (permissionCheck != null) return permissionCheck;
        
        List<User> users = userService.getAllUsers();
        // 去除敏感信息
        users.forEach(user -> user.setPassword(null));
        return RestBean.success("获取用户列表成功", users);
    }
    
    /**
     * 按用户名搜索用户（管理员专用）
     */
    @GetMapping("/admin/search")
    public RestBean<?> searchUsers(@RequestParam String keyword, HttpServletRequest request) {
        RestBean<String> permissionCheck = checkAdminPermission(request);
        if (permissionCheck != null) return permissionCheck;
        
        List<User> users = userService.searchUsersByUsername(keyword);
        // 去除敏感信息
        users.forEach(user -> user.setPassword(null));
        return RestBean.success("搜索用户成功", users);
    }
    
    /**
     * 获取单个用户信息（管理员专用）
     */
    @GetMapping("/admin/user/{id}")
    public RestBean<?> getUserById(@PathVariable Integer id, HttpServletRequest request) {
        RestBean<String> permissionCheck = checkAdminPermission(request);
        if (permissionCheck != null) return permissionCheck;
        
        User user = userService.getUserById(id);
        if (user == null) {
            return RestBean.failure(404, "用户不存在");
        }
        user.setPassword(null);
        return RestBean.success("获取用户信息成功", user);
    }
    
    /**
     * 添加新用户（管理员专用）
     */
    @PostMapping("/admin/add")
    public RestBean<String> addUser(@RequestBody User user, HttpServletRequest request) {
        RestBean<String> permissionCheck = checkAdminPermission(request);
        if (permissionCheck != null) return permissionCheck;
        
        // 检查用户名是否已存在
        if (userService.getUserByUsername(user.getUsername()) != null) {
            return RestBean.failure(400, "用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (user.getMail() != null && !user.getMail().isEmpty() && userService.getUserByMail(user.getMail()) != null) {
            return RestBean.failure(400, "邮箱已被使用");
        }
        
        // 检查学号是否已存在
        if (user.getStudentId() != null && !user.getStudentId().isEmpty() && userService.getUserByStudentId(user.getStudentId()) != null) {
            return RestBean.failure(400, "学号已被使用");
        }
        
        // 管理员直接添加的用户设置为已审核状态
        user.setStatus(1);
        
        // 使用完整用户对象创建用户
        if (userService.createUserComplete(user)) {
            return RestBean.success("添加用户成功");
        }
        return RestBean.failure(500, "添加用户失败");
    }
    
    /**
     * 更新用户信息（管理员专用）
     */
    @PutMapping("/admin/update")
    public RestBean<String> updateUser(@RequestBody User user, HttpServletRequest request) {
        RestBean<String> permissionCheck = checkAdminPermission(request);
        if (permissionCheck != null) return permissionCheck;
        
        // 检查用户是否存在
        if (userService.getUserById(user.getId()) == null) {
            return RestBean.failure(404, "用户不存在");
        }
        
        // 更新用户信息
        if (userService.updateUser(user)) {
            return RestBean.success("更新用户信息成功");
        }
        return RestBean.failure(500, "更新用户信息失败");
    }
    
    /**
     * 更新用户密码（管理员专用）
     */
    @PutMapping("/admin/update-password")
    public RestBean<String> updateUserPassword(@RequestParam Integer userId, 
                                              @RequestParam String newPassword,
                                              HttpServletRequest request) {
        RestBean<String> permissionCheck = checkAdminPermission(request);
        if (permissionCheck != null) return permissionCheck;
        
        // 检查用户是否存在
        if (userService.getUserById(userId) == null) {
            return RestBean.failure(404, "用户不存在");
        }
        
        // 更新密码
        if (userService.updatePassword(userId, newPassword)) {
            return RestBean.success("密码更新成功");
        }
        return RestBean.failure(500, "密码更新失败");
    }
    
    /**
     * 删除用户（管理员专用）
     */
    @DeleteMapping("/admin/delete/{id}")
    public RestBean<String> deleteUser(@PathVariable Integer id, HttpServletRequest request) {
        RestBean<String> permissionCheck = checkAdminPermission(request);
        if (permissionCheck != null) return permissionCheck;
        
        // 检查用户是否存在
        if (userService.getUserById(id) == null) {
            return RestBean.failure(404, "用户不存在");
        }
        
        // 删除用户
        if (userService.deleteUser(id)) {
            return RestBean.success("删除用户成功");
        }
        return RestBean.failure(500, "删除用户失败");
    }
    
    // =========================== 用户审核相关 ============================
    
    /**
     * 用户注册（创建待审核用户）
     */
    @PostMapping("/register")
    public RestBean<String> registerUser(@RequestBody Map<String, String> registerData) {
        String username = registerData.get("username");
        String password = registerData.get("password");
        String mail = registerData.get("mail");
        String studentId = registerData.get("studentId");
        
        // 检查用户名是否已存在
        if (userService.getUserByUsername(username) != null) {
            return RestBean.failure(400, "用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (mail != null && !mail.isEmpty() && userService.getUserByMail(mail) != null) {
            return RestBean.failure(400, "邮箱已被使用");
        }
        
        // 检查学号是否已存在
        if (studentId != null && !studentId.isEmpty() && userService.getUserByStudentId(studentId) != null) {
            return RestBean.failure(400, "学号已被使用");
        }
        
        // 创建待审核用户
        if (userService.registerUser(username, password, mail, studentId)) {
            return RestBean.success("注册成功，请等待管理员审核");
        }
        return RestBean.failure(500, "注册失败");
    }
    
    /**
     * 获取待审核用户列表（管理员专用）
     */
    @GetMapping("/admin/pending")
    public RestBean<?> getPendingUsers(HttpServletRequest request) {
        RestBean<String> permissionCheck = checkAdminPermission(request);
        if (permissionCheck != null) return permissionCheck;
        
        List<User> pendingUsers = userService.getPendingUsers();
        // 去除敏感信息
        pendingUsers.forEach(user -> user.setPassword(null));
        return RestBean.success("获取待审核用户列表成功", pendingUsers);
    }
    
    /**
     * 审核通过用户（管理员专用）
     */
    @PostMapping("/admin/approve/{id}")
    public RestBean<String> approveUser(@PathVariable Integer id, HttpServletRequest request) {
        RestBean<String> permissionCheck = checkAdminPermission(request);
        if (permissionCheck != null) return permissionCheck;
        
        // 检查用户是否存在
        if (userService.getUserById(id) == null) {
            return RestBean.failure(404, "用户不存在");
        }
        
        // 审核通过用户
        if (userService.approveUser(id)) {
            return RestBean.success("用户审核通过");
        }
        return RestBean.failure(500, "操作失败");
    }
    
    /**
     * 拒绝用户注册（管理员专用）
     */
    @PostMapping("/admin/reject/{id}")
    public RestBean<String> rejectUser(@PathVariable Integer id, HttpServletRequest request) {
        RestBean<String> permissionCheck = checkAdminPermission(request);
        if (permissionCheck != null) return permissionCheck;
        
        // 检查用户是否存在
        if (userService.getUserById(id) == null) {
            return RestBean.failure(404, "用户不存在");
        }
        
        // 拒绝用户注册
        if (userService.rejectUser(id)) {
            return RestBean.success("已拒绝用户注册");
        }
        return RestBean.failure(500, "操作失败");
    }
    
    /**
     * 检查用户名状态
     */
    @GetMapping("/check-status")
    public RestBean<?> checkUserStatus(@RequestParam String username) {
        Integer status = userService.getUserStatus(username);
        if (status == null) {
            return RestBean.failure(404, "用户不存在");
        }
        String message;
        switch (status) {
            case 0:
                message = "用户正在审核中";
                break;
            case 1:
                message = "用户已通过审核";
                break;
            case 2:
                message = "用户注册已被拒绝";
                break;
            default:
                message = "未知状态";
        }
        return RestBean.success(message, status);
    }
}
