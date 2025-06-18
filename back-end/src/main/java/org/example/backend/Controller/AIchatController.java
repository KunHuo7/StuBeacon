package org.example.backend.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.Entity.pojo.Conversation;
import org.example.backend.Entity.pojo.ConversationTag;
import org.example.backend.Entity.pojo.RestBean;
import org.example.backend.Entity.vo.ConversationVO;
import org.example.backend.Entity.vo.MessageVO;
import org.example.backend.Service.ConversationService;
import org.example.backend.Service.MessageService;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * AI聊天控制器
 */
@RestController
@RequestMapping("/api/chat")
public class AIchatController {
    
    @Resource
    private ConversationService conversationService;
    
    @Resource
    private MessageService messageService;
    
    /**
     * 获取用户的所有对话
     */
    @GetMapping("/conversations")
    public RestBean<List<ConversationVO>> getUserConversations(HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getAttribute("id");
            if (userId == null) {
                return RestBean.failure(401, "用户未登录");
            }
            
            List<ConversationVO> conversations = conversationService.getUserConversations(userId);
            return RestBean.success("获取对话列表成功", conversations);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure(500, "服务器错误");
        }
    }
    
    /**
     * 创建新对话
     */
    @PostMapping("/conversations")
    public RestBean<Integer> createConversation(
            HttpServletRequest request,
            @RequestParam("title") String title
    ) {
        try {
            Integer userId = (Integer) request.getAttribute("id");
            if (userId == null) {
                return RestBean.failure(401, "用户未登录");
            }
            
            Integer conversationId = conversationService.createConversation(title, userId);
            return RestBean.success("创建对话成功", conversationId);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure(500, "服务器错误");
        }
    }
    
    /**
     * 删除对话
     */
    @DeleteMapping("/conversations/{id}")
    public RestBean<Void> deleteConversation(
            HttpServletRequest request,
            @PathVariable("id") Integer conversationId
    ) {
        try {
            Integer userId = (Integer) request.getAttribute("id");
            if (userId == null) {
                return RestBean.failure(401, "用户未登录");
            }
            
            boolean success = conversationService.deleteConversation(conversationId, userId);
            
            if (success) {
                return RestBean.success("删除对话成功");
            } else {
                return RestBean.failure(403, "无权删除该对话或对话不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure(500, "服务器错误");
        }
    }
    
    /**
     * 获取对话中的所有消息
     */
    @GetMapping("/conversations/{id}/messages")
    public RestBean<List<MessageVO>> getConversationMessages(
            HttpServletRequest request,
            @PathVariable("id") Integer conversationId
    ) {
        try {
            Integer userId = (Integer) request.getAttribute("id");
            if (userId == null) {
                return RestBean.failure(401, "用户未登录");
            }
            
            List<MessageVO> messages = messageService.getConversationMessages(conversationId, userId);
            return RestBean.success("获取消息成功", messages);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure(500, "服务器错误");
        }
    }
    
    /**
     * 发送消息并获取AI回复
     */
    @PostMapping("/conversations/{id}/messages")
    public RestBean<MessageVO> sendMessage(
            HttpServletRequest request,
            @PathVariable("id") Integer conversationId,
            @RequestParam("content") String content
    ) {
        try {
            Integer userId = (Integer) request.getAttribute("id");
            if (userId == null) {
                return RestBean.failure(401, "用户未登录");
            }
            
            MessageVO messageVO = messageService.sendMessage(conversationId, content, userId);
            
            if (messageVO != null) {
                return RestBean.success("发送消息成功", messageVO);
            } else {
                return RestBean.failure(403, "发送消息失败，请检查对话ID是否正确");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure(500, "服务器错误: " + e.getMessage());
        }
    }
    
    /**
     * 生成对话摘要
     */
    @PostMapping("/conversations/{id}/summary")
    public RestBean<String> generateSummary(
            HttpServletRequest request,
            @PathVariable("id") Integer conversationId
    ) {
        try {
            Integer userId = (Integer) request.getAttribute("id");
            if (userId == null) {
                return RestBean.failure(401, "用户未登录");
            }
            
            String summary = conversationService.generateAndUpdateSummary(conversationId, userId);
            if (summary != null) {
                return RestBean.success("生成摘要成功", summary);
            } else {
                return RestBean.failure(403, "生成摘要失败，请检查对话ID是否正确或对话是否为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure(500, "服务器错误");
        }
    }
    
    /**
     * 为对话添加标签
     */
    @PostMapping("/conversations/{id}/tags")
    public RestBean<Void> addTagToConversation(
            HttpServletRequest request,
            @PathVariable("id") Integer conversationId,
            @RequestParam("tagName") String tagName
    ) {
        try {
            Integer userId = (Integer) request.getAttribute("id");
            if (userId == null) {
                return RestBean.failure(401, "用户未登录");
            }
            
            boolean success = conversationService.addTagToConversation(conversationId, tagName, userId);
            if (success) {
                return RestBean.success("添加标签成功");
            } else {
                return RestBean.failure(403, "添加标签失败，请检查对话ID是否正确");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure(500, "服务器错误");
        }
    }
    
    /**
     * 获取用户的所有标签
     */
    @GetMapping("/tags")
    public RestBean<List<ConversationTag>> getUserTags(HttpServletRequest request) {
        try {
            Integer userId = (Integer) request.getAttribute("id");
            if (userId == null) {
                return RestBean.failure(401, "用户未登录");
            }
            
            List<ConversationTag> tags = conversationService.getUserTags(userId);
            return RestBean.success("获取标签成功", tags);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure(500, "服务器错误");
        }
    }
    
    /**
     * 按标签查询对话
     */
    @GetMapping("/tags/{tagId}/conversations")
    public RestBean<List<ConversationVO>> getConversationsByTag(
            HttpServletRequest request,
            @PathVariable("tagId") Integer tagId
    ) {
        try {
            Integer userId = (Integer) request.getAttribute("id");
            if (userId == null) {
                return RestBean.failure(401, "用户未登录");
            }
            
            List<ConversationVO> conversations = conversationService.getConversationsByTag(tagId, userId);
            return RestBean.success("获取对话列表成功", conversations);
        } catch (Exception e) {
            e.printStackTrace();
            return RestBean.failure(500, "服务器错误");
        }
    }
} 