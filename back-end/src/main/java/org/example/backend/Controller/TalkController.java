package org.example.backend.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.Entity.pojo.Notification;
import org.example.backend.Entity.pojo.NotificationRS;
import org.example.backend.Entity.pojo.RestBean;
import org.example.backend.Entity.pojo.Talk;
import org.example.backend.Entity.pojo.TalkComment;
import org.example.backend.Entity.vo.TalkCommentVO;
import org.example.backend.Entity.vo.TalkVO;
import org.example.backend.Mapper.NotificationMapper;
import org.example.backend.Mapper.UserMapper;
import org.example.backend.Mapper.TalkMapper;
import org.example.backend.Mapper.TalkCommentMapper;
import org.example.backend.Service.TalkService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/talk")
public class TalkController {
    @Resource
    TalkService talkService;
    
    @Resource
    UserMapper userMapper;
    
    @Resource
    NotificationMapper notificationMapper;
    
    @Resource
    TalkMapper talkMapper;
    
    @Resource
    TalkCommentMapper talkCommentMapper;

    @PostMapping("create")
    public RestBean<String> createNewTalk(
            HttpServletRequest request,
            @RequestParam String title,        // 直接接收表单字段
            @RequestParam String content,
            @RequestParam String tags          // 接收逗号分隔的标签字符串
    ) {
        try {
//            System.out.println("创建话题请求 - 标题: " + title);
//            System.out.println("创建话题请求 - 内容: " + content);
//            System.out.println("创建话题请求 - 标签: " + tags);
//
//            // 将逗号分隔的 tags 字符串转为数组
            String[] tagsArray = tags.split(",");
//            System.out.println("标签数组长度: " + tagsArray.length);
//
//            // 打印标签数组内容
//            for (int i = 0; i < tagsArray.length; i++) {
//                System.out.println("标签[" + i + "]: " + tagsArray[i]);
//            }

            // 构建 Talk 对象
            Talk talk = new Talk(
                    title,
                    (Integer) request.getAttribute("id"),
                    content,
                    new Date()
            );
            talk.setTags(tagsArray);
//            System.out.println("创建的Talk对象: " + talk);

            // 调用服务层
            int result = talkService.addNewTalk(talk);
//            System.out.println("添加话题结果: " + result);
            
            if (result > 0) {
//                System.out.println("话题创建成功，ID: " + talk.getId());
                return RestBean.success("话题发布成功");
            } else {
//                System.out.println("话题创建失败");
                return RestBean.failure(503, "出现错误，请联系管理员");
            }
        } catch (Exception e) {
//            System.out.println("创建话题异常: " + e.getClass().getName());
//            System.out.println("异常信息: " + e.getMessage());
            e.printStackTrace();
            return RestBean.failure(500, "创建话题失败: " + e.getMessage());
        }
    }

    @GetMapping("list")
    public RestBean<List<TalkVO>> getTalkList() {
//        System.out.println("获取话题列表请求");
        List<TalkVO> talks = talkService.getOrderByTalks();
//        System.out.println("获取到话题数量: " + (talks != null ? talks.size() : 0));
        return RestBean.success("获取成功", talks);
    }

    @GetMapping("detail")
    public RestBean<TalkVO> getTalkDetail(
            @RequestParam("id") int id,
            HttpServletRequest request
    ) {
//        System.out.println("获取话题详情请求 - 话题ID: " + id);
        Integer userId = (Integer) request.getAttribute("id");
//        System.out.println("当前用户ID: " + userId);
        
        TalkVO talkVO = talkService.getTalkById(id, userId);
        if (talkVO != null) {
//            System.out.println("获取话题详情成功 - 标题: " + talkVO.getTalk().getTitle());
            return RestBean.success("获取成功", talkVO);
        } else {
//            System.out.println("获取话题详情失败 - 话题不存在");
            return RestBean.failure(404, "话题不存在");
        }
    }

    @PostMapping("like")
    public RestBean<String> likeTalk(
            @RequestParam("id") int id,
            HttpServletRequest request
    ) {
//        System.out.println("点赞话题请求 - 话题ID: " + id);
        int userId = (Integer) request.getAttribute("id");
        System.out.println("当前用户ID: " + userId);
        
        String s = talkService.doNewLike(userId, id);
        if (s == null) {
//            System.out.println("点赞话题成功");
            return RestBean.success("点赞成功");
        } else {
//            System.out.println("点赞话题失败: " + s);
            return RestBean.failure(500, s);
        }
    }

    @PostMapping("unlike")
    public RestBean<String> unlikeTalk(
            @RequestParam("id") int id,
            HttpServletRequest request
    ) {
//        System.out.println("取消点赞话题请求 - 话题ID: " + id);
        int userId = (Integer) request.getAttribute("id");
//        System.out.println("当前用户ID: " + userId);
        
        String s = talkService.deleteLike(userId, id);
        if (s == null) {
//            System.out.println("取消点赞话题成功");
            return RestBean.success("取消点赞成功");
        } else {
//            System.out.println("取消点赞话题失败: " + s);
            return RestBean.failure(500, s);
        }
    }

    @GetMapping("check-like")
    public RestBean<Integer> checkLikedTalk(
            @RequestParam("id") int id,
            HttpServletRequest request
    ) {
//        System.out.println("检查话题点赞状态请求 - 话题ID: " + id);
        int userId = (Integer) request.getAttribute("id");
//        System.out.println("当前用户ID: " + userId);
        
        int result = talkService.checkUserLikedTalk(userId, id);
//        System.out.println("检查结果: " + result);
        return RestBean.success("", result);
    }

    @GetMapping("by-tag")
    public RestBean<List<TalkVO>> getTalksByTag(
            @RequestParam("tag") String tag
    ) {
//        System.out.println("按标签获取话题请求 - 标签: " + tag);
        List<TalkVO> talks = talkService.getTalksByTag(tag);
//        System.out.println("获取到话题数量: " + (talks != null ? talks.size() : 0));
        return RestBean.success("获取成功", talks);
    }

    @PostMapping("comment")
    public RestBean<String> addComment(
            HttpServletRequest request,
            @RequestParam("talkId") int talkId,
            @RequestParam("content") String content,
            @RequestParam(value = "parentId", required = false) Integer parentId
    ) {
//        System.out.println("添加评论请求 - 话题ID: " + talkId);
//        System.out.println("评论内容: " + content);
//        System.out.println("回复评论ID: " + parentId);
        int userId = (Integer) request.getAttribute("id");
//        System.out.println("当前用户ID: " + userId);
        
        TalkComment comment;
        if (parentId != null && parentId > 0) {
            comment = new TalkComment(userId, talkId, content, new Date(), parentId);
        } else {
            comment = new TalkComment(userId, talkId, content, new Date());
        }
        
        int result = talkService.addNewComment(comment);
//        System.out.println("添加评论结果: " + result);
        
        if (result > 0) {
//            System.out.println("评论添加成功，ID: " + comment.getId());
            return RestBean.success("评论成功");
        } else {
//            System.out.println("评论添加失败");
            return RestBean.failure(503, "评论失败，请联系管理员");
        }
    }

    @GetMapping("comments")
    public RestBean<List<TalkCommentVO>> getComments(
            @RequestParam("talkId") int talkId,
            HttpServletRequest request
    ) {
//        System.out.println("获取评论列表请求 - 话题ID: " + talkId);
        int userId = (Integer) request.getAttribute("id");
//        System.out.println("当前用户ID: " + userId);
        
        List<TalkCommentVO> comments = talkService.getCommentsByTalkId(talkId, userId);
//        System.out.println("获取到评论数量: " + (comments != null ? comments.size() : 0));
        return RestBean.success("获取成功", comments);
    }

    @GetMapping("searchByTitle")
    public RestBean<Map<String, Integer>> searchTalkByTitle(
            @RequestParam("title") String title
    ) {
//        System.out.println("根据标题查询话题请求 - 标题: " + title);
        Integer talkId = talkService.findTalkIdByTitle(title);
//        System.out.println("查询结果ID: " + talkId);
        
        Map<String, Integer> result = new HashMap<>();
        result.put("id", talkId);
        
        return RestBean.success("获取成功", result);
    }
    
    @DeleteMapping("delete")
    public RestBean<String> deleteTalk(
            @RequestParam("id") int id,
            HttpServletRequest request
    ) {
        int userId = (Integer) request.getAttribute("id");
        
        // 获取删除前的话题信息（用户名和话题标题）
        TalkVO talkVO = talkService.getTalkById(id, userId);
        if (talkVO == null) {
            return RestBean.failure(404, "话题不存在");
        }
        
        String talkTitle = talkVO.getTalk().getTitle();
        int authorId = talkVO.getTalk().getAuthorId();
        String deleterName = userMapper.getUsernameById(userId);
        
        boolean result = talkService.deleteTalk(id, userId);
        if (result) {
            // 如果不是作者自己删除，才发送通知
            if (userId != authorId) {
                // 创建当前时间
                Date currentTime = new Date();
                
                // 创建通知内容
                Notification notification = new Notification(
                    1, // 假设删除通知类型为1
                    "话题删除通知",
                    "您的话题: \"" + talkTitle + "\" 被用户: " + deleterName + " 删除了",
                    currentTime
                );
                
                // 添加通知记录
                notificationMapper.insertNewNotificationData(notification);
                
                // 创建通知关系（发送给话题作者）
                NotificationRS notificationRS = new NotificationRS(authorId, notification.getId(), false);
                notificationMapper.insertNewNotificationRS(notificationRS);
            }
            return RestBean.success("话题删除成功");
        } else {
            return RestBean.failure(403, "无权删除此话题或话题不存在");
        }
    }

    @DeleteMapping("comment/delete")
    public RestBean<String> deleteComment(
            @RequestParam("id") int id,
            HttpServletRequest request
    ) {
        int userId = (Integer) request.getAttribute("id");
        
        // 获取评论的信息 - 直接使用talkCommentMapper而不是talkService
        TalkComment comment = talkCommentMapper.getCommentById(id);
        if (comment == null) {
            return RestBean.failure(404, "评论不存在");
        }
        
        // 获取相关信息：话题标题、评论作者ID和评论删除者用户名 - 直接使用talkMapper而不是talkService
        Talk talk = talkMapper.getTalkById(comment.getTalkId());
        if (talk == null) {
            return RestBean.failure(404, "相关话题不存在");
        }
        
        String talkTitle = talk.getTitle();
        int commentAuthorId = comment.getAuthorId();
        String deleterName = userMapper.getUsernameById(userId);
        
        boolean result = talkService.deleteComment(id, userId);
        if (result) {
            // 如果不是评论作者自己删除，才发送通知
            if (userId != commentAuthorId) {
                // 创建当前时间
                Date currentTime = new Date();
                
                // 创建通知内容
                Notification notification = new Notification(
                    1, // 假设删除通知类型为1
                    "评论删除通知",
                    "您在话题: \"" + talkTitle + "\" 下的评论被用户: " + deleterName + " 删除了",
                    currentTime
                );
                
                // 添加通知记录
                notificationMapper.insertNewNotificationData(notification);
                
                // 创建通知关系（发送给评论作者）
                NotificationRS notificationRS = new NotificationRS(commentAuthorId, notification.getId(), false);
                notificationMapper.insertNewNotificationRS(notificationRS);
            }
            return RestBean.success("评论删除成功");
        } else {
            return RestBean.failure(403, "无权删除此评论或评论不存在");
        }
    }
}