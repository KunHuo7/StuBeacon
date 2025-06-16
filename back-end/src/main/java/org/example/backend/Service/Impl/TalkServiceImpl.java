package org.example.backend.Service.Impl;

import jakarta.annotation.Resource;
import org.example.backend.Entity.pojo.Notification;
import org.example.backend.Entity.pojo.NotificationRS;
import org.example.backend.Entity.pojo.Talk;
import org.example.backend.Entity.pojo.TalkComment;
import org.example.backend.Entity.pojo.User;
import org.example.backend.Entity.vo.TalkCommentVO;
import org.example.backend.Entity.vo.TalkVO;
import org.example.backend.Mapper.NotificationMapper;
import org.example.backend.Mapper.TalkCommentMapper;
import org.example.backend.Mapper.TalkLikeMapper;
import org.example.backend.Mapper.TalkMapper;
import org.example.backend.Mapper.UserMapper;
import org.example.backend.Service.TalkService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TalkServiceImpl implements TalkService {

    @Resource
    private TalkMapper talkMapper;
    
    @Resource
    private TalkLikeMapper talkLikeMapper;
    
    @Resource
    private TalkCommentMapper talkCommentMapper;
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private NotificationMapper notificationMapper;
    
    @Override
    @Transactional
    public int addNewTalk(Talk talk) {
        if (talk.getViews() == null) talk.setViews(0);
        if (talk.getLikes() == null) talk.setLikes(0);
        if (talk.getComments() == null) talk.setComments(0);
        
        return talkMapper.insertNewTalk(talk);
    }
    
    @Override
    public List<TalkVO> getOrderByTalks() {
        List<Talk> talks = talkMapper.getTalksOrderById();
        List<TalkVO> talkVOs = new ArrayList<>();
        for (Talk talk : talks) {
            if (talk == null || talk.getAuthorId() == null) continue;
            
            User author = userMapper.getUserById(talk.getAuthorId());
            if (author == null) continue;
            
            TalkVO talkVO = new TalkVO();
            talkVO.setTalk(talk);
            talkVO.setUsername(author.getUsername());
            talkVO.setAvatar(author.getAvatar());
            talkVOs.add(talkVO);
        }
        return talkVOs;
    }
    
    @Override
    @Transactional
    public TalkVO getTalkById(int id, int userId) {
        // 增加浏览量
        talkMapper.updateViewsById(id);
        
        Talk talk = talkMapper.getTalkById(id);
        if (talk == null) {
            return null;
        }
        
        User author = userMapper.getUserById(talk.getAuthorId());
        if (author == null) {
            return null;
        }
        
        TalkVO talkVO = new TalkVO();
        talkVO.setTalk(talk);
        talkVO.setUsername(author.getUsername());
        talkVO.setAvatar(author.getAvatar());
        
        // 检查用户是否点赞过
        Integer likeId = talkLikeMapper.checkUserLikedTalk(userId, id);
        talkVO.setIsLiked(likeId != null);
        
        return talkVO;
    }
    
    @Override
    @Transactional
    public String doNewLike(Integer userId, Integer talkId) {
        if (talkLikeMapper.checkUserLikedTalk(userId, talkId) != null) {
            return "您已经点赞过该话题";
        }
        
        if (talkLikeMapper.addNewLikeRecord(userId, talkId) == 1 && talkMapper.addLikesById(talkId) == 1) {
            Talk talk = talkMapper.getTalkById(talkId);
            // 创建当前时间对象
            Date currentTime = new Date();
            String username = userMapper.getUsernameById(userId);
            Notification notification = new Notification(0, "话题点赞通知", 
                    "您的话题: \"" + talk.getTitle() + "\" 刚刚被用户:" + username + "点赞了",
                    currentTime);
            // 添加日志记录
            System.out.println("创建点赞通知：" + notification.getTitle() + ", 时间: " + notification.getCreateTime());
            int result = notificationMapper.insertNewNotificationData(notification);
            if(result <= 0) {
                System.out.println("插入点赞通知失败");
                return "发生错误，请重试";
            }
            int rsResult = notificationMapper.insertNewNotificationRS(new NotificationRS(talk.getAuthorId(), notification.getId(), false));
            if(rsResult <= 0) {
                System.out.println("插入通知关系失败");
                return "发生错误，请重试";
            }
            return null;
        } else {
            return "发生错误，请重试";
        }
    }
    
    @Override
    @Transactional
    public String deleteLike(Integer userId, Integer talkId) {
        if (talkLikeMapper.checkUserLikedTalk(userId, talkId) == null) {
            return "您尚未点赞该话题";
        }
        
        if (talkLikeMapper.deleteLikeRecord(userId, talkId) == 1 && talkMapper.unlikeById(talkId) == 1) {
            return null;
        } else {
            return "发生错误，请联系管理员";
        }
    }
    
    @Override
    public int checkUserLikedTalk(Integer userId, Integer talkId) {
        return talkLikeMapper.checkUserLikedTalk(userId, talkId) != null ? 1 : 0;
    }
    
    @Override
    public List<TalkVO> getTalksByTag(String tag) {
        if (tag == null || tag.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Talk> talks = talkMapper.getTalksByTag(tag);
        List<TalkVO> talkVOs = new ArrayList<>();
        for (Talk talk : talks) {
            if (talk == null || talk.getAuthorId() == null) continue;
            
            User author = userMapper.getUserById(talk.getAuthorId());
            if (author == null) continue;
            
            TalkVO talkVO = new TalkVO();
            talkVO.setTalk(talk);
            talkVO.setUsername(author.getUsername());
            talkVO.setAvatar(author.getAvatar());
            talkVOs.add(talkVO);
        }
        return talkVOs;
    }
    
    @Override
    @Transactional
    public int addNewComment(TalkComment comment) {
        try {
            // 检查话题是否存在
            Talk talk = talkMapper.getTalkById(comment.getTalkId());
            if (talk == null) {
                return 0;
            }
            
            if (comment.getLikes() == null) {
                comment.setLikes(0);
            }
            
            // 添加评论
            int commentResult = talkCommentMapper.insertNewComment(comment);
            if (commentResult <= 0) {
                return 0;
            }
            
            // 更新话题评论数
            int updateResult = talkMapper.incrementCommentsById(comment.getTalkId());
            if (updateResult <= 0) {
                return 0;
            }
            
            // 检查是否为回复评论
            if (comment.getParentId() != null && comment.getParentId() > 0) {
                // 获取被回复的评论
                TalkComment parentComment = talkCommentMapper.getCommentById(comment.getParentId());
                if (parentComment != null) {
                    // 获取被回复评论的作者
                    User parentAuthor = userMapper.getUserById(parentComment.getAuthorId());
                    // 如果不是自己回复自己，则发送通知
                    if (parentAuthor != null && !comment.getAuthorId().equals(parentComment.getAuthorId())) {
                        User currentUser = userMapper.getUserById(comment.getAuthorId());
                        String username = (currentUser != null) ? currentUser.getUsername() : "用户";
                        
                        Notification notification = new Notification(
                            3, 
                            "评论回复通知", 
                            "用户 " + username + " 回复了您在话题 \"" + talk.getTitle() + "\" 下的评论", 
                            new Date());
                        
                        int notificationResult = notificationMapper.insertNewNotificationData(notification);
                        if (notificationResult <= 0) {
                            return 0;
                        }
                        
                        int rsResult = notificationMapper.insertNewNotificationRS(
                            new NotificationRS(parentComment.getAuthorId(), notification.getId(), false));
                        if (rsResult <= 0) {
                            return 0;
                        }
                    }
                }
            }
            // 如果是直接评论话题（不是回复评论），并且不是作者自己评论，则添加通知
            else if (!comment.getAuthorId().equals(talk.getAuthorId())) {
                Notification notification = new Notification(0, "话题评论通知", 
                        "您的话题: \"" + talk.getTitle() + "\" 收到了新评论", 
                        new Date());
                int notificationResult = notificationMapper.insertNewNotificationData(notification);
                if (notificationResult <= 0) {
                    return 0;
                }
                
                int rsResult = notificationMapper.insertNewNotificationRS(new NotificationRS(talk.getAuthorId(), notification.getId(), false));
                if (rsResult <= 0) {
                    return 0;
                }
            }
            
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public List<TalkCommentVO> getCommentsByTalkId(Integer talkId, Integer userId) {
        List<TalkComment> comments = talkCommentMapper.getCommentsByTalkId(talkId);
        List<TalkCommentVO> commentVOs = new ArrayList<>();
        
        for (TalkComment comment : comments) {
            if (comment == null || comment.getAuthorId() == null) continue;
            
            User author = userMapper.getUserById(comment.getAuthorId());
            if (author == null) continue;
            
            TalkCommentVO commentVO = new TalkCommentVO();
            commentVO.setComment(comment);
            commentVO.setAuthor(author.getUsername());
            commentVO.setAvatar(author.getAvatar());
            commentVOs.add(commentVO);
        }
        
        return commentVOs;
    }

    @Override
    public Integer findTalkIdByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return null;
        }
        
        List<Talk> talks = talkMapper.getTalksOrderById();
        for (Talk talk : talks) {
            if (talk.getTitle() != null && talk.getTitle().contains(title)) {
                return talk.getId();
            }
        }
        return null;
    }
    
    @Override
    @Transactional
    public boolean deleteTalk(Integer talkId, Integer userId) {
        try {
            // 获取话题信息
            Talk talk = talkMapper.getTalkById(talkId);
            if (talk == null) {
                return false;
            }
            
            // 检查是否为话题作者
            boolean isAuthor = talk.getAuthorId().equals(userId);
            
            // 检查是否为管理员
            User user = userMapper.getUserById(userId);
            boolean isAdmin = user != null && user.getManager() != null && user.getManager() == 1;
            
            // 只有话题作者或管理员才能删除
            if (!isAuthor && !isAdmin) {
                return false;
            }
            
            // 删除话题
            return talkMapper.deleteTalkById(talkId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    @Transactional
    public boolean deleteComment(Integer commentId, Integer userId) {
        try {
            // 获取评论信息
            TalkComment comment = talkCommentMapper.getCommentById(commentId);
            if (comment == null) {
                return false;
            }
            
            // 获取话题信息
            Talk talk = talkMapper.getTalkById(comment.getTalkId());
            if (talk == null) {
                return false;
            }
            
            // 检查权限：
            // 1. 评论作者可以删除自己的评论
            // 2. 话题作者可以删除自己话题下的任何评论
            // 3. 管理员可以删除任何评论
            boolean isCommentAuthor = comment.getAuthorId().equals(userId);
            boolean isTalkAuthor = talk.getAuthorId().equals(userId);
            
            // 检查是否为管理员
            User user = userMapper.getUserById(userId);
            boolean isAdmin = user != null && user.getManager() != null && user.getManager() == 1;
            
            if (!isCommentAuthor && !isTalkAuthor && !isAdmin) {
                return false;
            }
            
            // 删除评论
            if (talkCommentMapper.deleteCommentById(commentId) > 0) {
                // 更新话题评论数
                talkMapper.decrementCommentsById(comment.getTalkId());
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}