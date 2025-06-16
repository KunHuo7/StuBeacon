package org.example.backend.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.Entity.pojo.CampusActivity;
import org.example.backend.Entity.pojo.RestBean;
import org.example.backend.Entity.pojo.User;
import org.example.backend.Entity.requestbodies.ActivityRequest;
import org.example.backend.Service.CampusActivityService;
import org.example.backend.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/activity")
public class CampusActivityController {
    
    @Resource
    private CampusActivityService activityService;
    
    @Resource
    private UserService userService;
    
    /**
     * 发布校园活动
     * @param request 活动请求体
     * @param httpRequest HTTP请求
     * @return 发布结果
     */
    @PostMapping("/publish")
    public RestBean<Integer> publishActivity(@RequestBody ActivityRequest request, HttpServletRequest httpRequest) {
        try {
            Integer userId = (Integer) httpRequest.getAttribute("id");
            if (userId == null) {
                return RestBean.failure(401, "未授权，请先登录");
            }
            
            if (request == null) {
                return RestBean.failure(400, "活动数据为空");
            }
            
            // 校验必填字段
            if (request.getActivityName() == null || request.getLocation() == null || 
                request.getOrganizer() == null || request.getStartTime() == null || 
                request.getEndTime() == null || request.getMaxParticipants() == null) {
                return RestBean.failure(400, "活动信息不完整");
            }
            
            Integer activityId = activityService.publishActivity(request, userId);
            if (activityId != null && activityId > 0) {
                return RestBean.success("活动发布成功，等待审核", activityId);
            }
            return RestBean.failure(500, "活动发布失败");
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure(500, "活动发布异常: " + e.getMessage());
        }
    }
    
    /**
     * 获取活动详情
     * @param id 活动ID
     * @return 活动详情
     */
    @GetMapping("/detail")
    public RestBean<CampusActivity> getActivityDetail(@RequestParam("id") Integer id) {
        CampusActivity activity = activityService.getActivityById(id);
        if (activity != null) {
            return RestBean.success("获取成功", activity);
        }
        return RestBean.failure(404, "活动不存在");
    }
    
    /**
     * 获取活动发布者信息
     * @param id 活动ID
     * @return 发布者信息
     */
    @GetMapping("/creator")
    public RestBean<User> getActivityCreator(@RequestParam("id") Integer id) {
        CampusActivity activity = activityService.getActivityById(id);
        if (activity != null && activity.getUserId() != null) {
            User creator = userService.getUserById(activity.getUserId());
            if (creator != null) {
                // 移除敏感信息
                creator.setPassword(null);
                return RestBean.success("获取成功", creator);
            }
            return RestBean.failure(404, "未找到发布者信息");
        }
        return RestBean.failure(404, "活动不存在");
    }
    
    /**
     * 获取活动参与者列表
     * @param id 活动ID
     * @return 参与者列表
     */
    @GetMapping("/participants")
    public RestBean<List<User>> getActivityParticipants(@RequestParam("id") Integer id) {
        List<User> participants = activityService.getActivityParticipants(id);
        return RestBean.success("获取成功", participants);
    }
    
    /**
     * 获取所有已批准的活动
     * @return 活动列表
     */
    @GetMapping("/list")
    public RestBean<List<CampusActivity>> getApprovedActivities() {
        return RestBean.success("获取成功", activityService.getAllApprovedActivities());
    }
    
    /**
     * 获取待审核的活动（需要管理员权限）
     * @param httpRequest HTTP请求
     * @return 待审核活动列表
     */
    @GetMapping("/pending")
    public RestBean<List<CampusActivity>> getPendingActivities(HttpServletRequest httpRequest) {
        Integer userId = (Integer) httpRequest.getAttribute("id");
        User user = userService.getUserById(userId);
        
        if (user.getManager() != null && user.getManager() == 1) {
            return RestBean.success("获取成功", activityService.getPendingActivities());
        }
        return RestBean.failure(403, "权限不足");
    }
    
    /**
     * 获取用户发布的活动
     * @param httpRequest HTTP请求
     * @return 用户发布的活动列表
     */
    @GetMapping("/my")
    public RestBean<List<CampusActivity>> getMyActivities(HttpServletRequest httpRequest) {
        Integer userId = (Integer) httpRequest.getAttribute("id");
        return RestBean.success("获取成功", activityService.getUserActivities(userId));
    }
    
    /**
     * 更新活动信息
     * @param request 活动请求体
     * @param httpRequest HTTP请求
     * @return 更新结果
     */
    @PostMapping("/update")
    public RestBean<String> updateActivity(@RequestBody ActivityRequest request, HttpServletRequest httpRequest) {
        Integer userId = (Integer) httpRequest.getAttribute("id");
        if (activityService.updateActivity(request, userId)) {
            return RestBean.success("活动更新成功");
        }
        return RestBean.failure(403, "活动更新失败，您可能没有权限或活动不存在");
    }
    
    /**
     * 审核活动（需要管理员权限）
     * @param requestBody 请求体，包含活动ID和状态
     * @param httpRequest HTTP请求
     * @return 审核结果
     */
    @PostMapping("/review")
    public RestBean<String> reviewActivity(
            @RequestBody Map<String, Integer> requestBody,
            HttpServletRequest httpRequest) {
        
        Integer id = requestBody.get("id");
        Integer status = requestBody.get("status");
        
        if (id == null || status == null) {
            return RestBean.failure(400, "参数不完整");
        }
        
        Integer userId = (Integer) httpRequest.getAttribute("id");
        User user = userService.getUserById(userId);
        
        if (user.getManager() != null && user.getManager() == 1) {
            if (activityService.reviewActivity(id, status)) {
                return RestBean.success("审核完成");
            }
            return RestBean.failure(500, "审核失败，活动不存在");
        }
        return RestBean.failure(403, "权限不足");
    }
    
    /**
     * 删除活动
     * @param requestBody 请求体，包含活动ID
     * @param httpRequest HTTP请求
     * @return 删除结果
     */
    @PostMapping("/delete")
    public RestBean<String> deleteActivity(@RequestBody Map<String, Integer> requestBody, HttpServletRequest httpRequest) {
        Integer id = requestBody.get("id");
        if (id == null) {
            return RestBean.failure(400, "参数不完整");
        }
        
        Integer userId = (Integer) httpRequest.getAttribute("id");
        if (activityService.deleteActivity(id, userId)) {
            return RestBean.success("活动删除成功");
        }
        return RestBean.failure(403, "活动删除失败，您可能没有权限或活动不存在");
    }
    
    /**
     * 参加活动
     * @param requestBody 请求体，包含活动ID
     * @param httpRequest HTTP请求
     * @return 参加结果
     */
    @PostMapping("/join")
    public RestBean<String> joinActivity(@RequestBody Map<String, Integer> requestBody, HttpServletRequest httpRequest) {
        Integer id = requestBody.get("id");
        if (id == null) {
            return RestBean.failure(400, "参数不完整");
        }
        
        Integer userId = (Integer) httpRequest.getAttribute("id");
//        System.out.println("用户(" + userId + ")尝试参加活动(ID: " + id + ")");
        
        if (activityService.joinActivity(id, userId)) {
//            System.out.println("用户(" + userId + ")成功参加活动(ID: " + id + ")");
            return RestBean.success("成功参加活动");
        }
//        System.out.println("用户(" + userId + ")参加活动(ID: " + id + ")失败");
        return RestBean.failure(400, "参加活动失败，可能活动已满、未审核通过或您已参加");
    }
    
    /**
     * 退出活动
     * @param requestBody 请求体，包含活动ID
     * @param httpRequest HTTP请求
     * @return 退出结果
     */
    @PostMapping("/quit")
    public RestBean<String> quitActivity(@RequestBody Map<String, Integer> requestBody, HttpServletRequest httpRequest) {
        Integer id = requestBody.get("id");
        if (id == null) {
            return RestBean.failure(400, "参数不完整");
        }
        
        Integer userId = (Integer) httpRequest.getAttribute("id");
//        System.out.println("用户(" + userId + ")尝试退出活动(ID: " + id + ")");
        
        if (activityService.quitActivity(id, userId)) {
//            System.out.println("用户(" + userId + ")成功退出活动(ID: " + id + ")");
            return RestBean.success("成功退出活动");
        }
//        System.out.println("用户(" + userId + ")退出活动(ID: " + id + ")失败");
        return RestBean.failure(400, "退出活动失败，您可能未参加此活动");
    }
    
    /**
     * 检查用户是否已参加活动
     * @param id 活动ID
     * @param httpRequest HTTP请求
     * @return 参加状态
     */
    @GetMapping("/check-joined")
    public RestBean<Boolean> checkJoined(@RequestParam("id") Integer id, HttpServletRequest httpRequest) {
        Integer userId = (Integer) httpRequest.getAttribute("id");
        boolean joined = activityService.checkUserJoined(id, userId);
        return RestBean.success("获取成功", joined);
    }
    
    /**
     * 获取用户参加的所有活动
     * @param httpRequest HTTP请求
     * @return 用户参加的活动列表
     */
    @GetMapping("/joined")
    public RestBean<List<CampusActivity>> getJoinedActivities(HttpServletRequest httpRequest) {
        Integer userId = (Integer) httpRequest.getAttribute("id");
//        System.out.println("获取用户(" + userId + ")参加的活动列表");
        
        List<CampusActivity> joinedActivities = activityService.getUserJoinedActivities(userId);
//        System.out.println("用户(" + userId + ")参加的活动数量: " + joinedActivities.size());
        
        return RestBean.success("获取成功", joinedActivities);
    }
} 