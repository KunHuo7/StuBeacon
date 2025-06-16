package org.example.backend.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.Entity.pojo.Personal;
import org.example.backend.Entity.pojo.RestBean;
import org.example.backend.Service.PersonalService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/personal")
public class PersonalController {
    @Resource
    PersonalService personalService;

    @PostMapping("/add")
    public RestBean<String> addPersonal(@RequestBody Personal personalInfo,
                                      HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return RestBean.failure(401, "未登录");
        }
        
        boolean result = personalService.addPersonal(personalInfo);
        return result ? RestBean.success("添加个人信息成功", "添加个人信息成功")
                    : RestBean.failure(500, "添加个人信息失败");
    }

    @PutMapping("/update")
    public RestBean<String> updatePersonal(@RequestBody Personal personalInfo,
                                         HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return RestBean.failure(401, "未登录");
        }
        
        boolean result = personalService.updatePersonal(personalInfo);
        return result ? RestBean.success("更新个人信息成功", "更新个人信息成功")
                    : RestBean.failure(500, "更新个人信息失败");
    }

    @GetMapping("/get")
    public RestBean<Personal> getPersonal(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            return RestBean.failure(401, "未登录");
        }
        
        Integer userId = Integer.parseInt(request.getHeader("UserId"));
//        System.out.println("正在获取用户ID: " + userId + " 的个人信息");

        Personal personalInfo = personalService.getPersonal(userId);
        if (personalInfo != null) {
            return RestBean.success("获取个人信息成功", personalInfo);
        } else {
//            System.out.println("未找到用户ID: " + userId + " 的个人信息");
            return RestBean.failure(404, "未找到该学生的个人信息");
        }
    }
}
