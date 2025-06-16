package org.example.backend.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.example.backend.Entity.pojo.RestBean;
import org.example.backend.Entity.pojo.User;
import org.example.backend.Service.UserService;
import org.example.backend.util.JWTUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    @Resource
    UserService userService;

    //get post两种请求方法
    @PostMapping("login")
    public RestBean<String> login(@RequestParam String username, @RequestParam String password, HttpSession session){
        // 检查用户状态
        Integer status = userService.getUserStatus(username);
        
        // 如果用户不存在，状态为null
        if (status == null) {
            return RestBean.failure(401, "账号不存在");
        }
        
        // 检查用户状态
        if (status == 0) {
            return RestBean.failure(403, "账号正在审核中，请等待管理员审核");
        } else if (status == 2) {
            return RestBean.failure(403, "账号审核未通过，请联系管理员");
        }
        
        // 只有审核通过的用户才能登录
        User loginUser = userService.loginApprovedUser(username, password);
        if (loginUser == null){
            return RestBean.failure(401, "账号密码错误");
        }
        else {
            return RestBean.success("登录成功", JWTUtil.createToken(loginUser));
        }
    }

    @PostMapping("register")
    public RestBean<String> register(@RequestParam String username,
                                     @RequestParam String password,
                                     @RequestParam String mail,
                                     @RequestParam String studentId){
        if(username == null || username.isEmpty() || password == null || password.isEmpty() ||
           mail == null || mail.isEmpty() || studentId == null || studentId.isEmpty()){
            return RestBean.failure(400, "用户名、密码、邮箱和学号不能为空");
        }
        if(userService.getUserByUsername(username) != null){
            return RestBean.failure(400, "用户名已存在");
        }
        if(userService.getUserByMail(mail) != null){
            return RestBean.failure(400, "该邮箱已被注册");
        }
        if(userService.getUserByStudentId(studentId) != null){
            return RestBean.failure(400, "该学号已被注册");
        }
        try {
            // 注册待审核用户
            if(userService.registerUser(username, password, mail, studentId)){
                return RestBean.success("注册成功，请等待管理员审核");
            }else{
                return RestBean.failure(500, "注册失败，请联系管理员");
            }
        } catch (Exception e) {
            return RestBean.failure(500, "服务器内部错误");
        }
    }

}
