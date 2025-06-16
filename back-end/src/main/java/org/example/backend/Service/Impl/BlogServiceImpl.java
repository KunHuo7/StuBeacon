package org.example.backend.Service.Impl;

import jakarta.annotation.Resource;
import org.example.backend.Entity.pojo.Blog;
import org.example.backend.Entity.pojo.Notification;
import org.example.backend.Entity.pojo.NotificationRS;
import org.example.backend.Entity.pojo.User;
import org.example.backend.Entity.vo.BlogVO;
import org.example.backend.Mapper.BlogMapper;
import org.example.backend.Mapper.LikeMapper;
import org.example.backend.Mapper.NotificationMapper;
import org.example.backend.Mapper.UserMapper;
import org.example.backend.Service.BlogService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class BlogServiceImpl implements BlogService {
    @Resource
    BlogMapper blogMapper;
    @Resource
    LikeMapper likeMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    NotificationMapper notificationMapper;

    @Override
    public int addNewBlog(Blog blog) {
        return blogMapper.insertNewBlog(blog);
    }

    @Override
    public List<BlogVO> getPublicBlogs() {
        try {
        List<BlogVO> result = new ArrayList<>();
            List<Blog> blogs = blogMapper.getPublicBlogsOrderById();
            
            if (blogs == null) {
                return result; // 返回空列表
            }
            
            for(Blog blog : blogs) {
                // 防止空指针异常
                Integer authorId = blog.getAuthorId();
                if (authorId == null) {
                    continue; // 跳过无效数据
                }
                
                String username = userMapper.getUsernameById(authorId);
                String avatar = userMapper.getAvatarById(authorId);
                
                // 所有字段都不为null时才添加
                if (blog != null && username != null) {
                    result.add(new BlogVO(blog, username, avatar));
                }
            }
            return result;
        } catch (Exception e) {
            System.err.println("获取公开博客时发生异常: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // 返回空列表
        }
    }
    
    @Override
    public List<BlogVO> getAllBlogs(int userId) {
        try {
            // 先检查是否是管理员
            boolean isAdmin = isAdminUser(userId);
            if (!isAdmin) {
            return getPublicBlogs();
        }
        
            // 如果是管理员，获取所有博客（包括私密）
        List<BlogVO> result = new ArrayList<>();
            List<Blog> blogs = blogMapper.getAllBlogsOrderById();
            if (blogs == null) {
                return new ArrayList<>(); // 返回空列表而不是null
            }
            
            for(Blog blog : blogs) {
                // 检查防止userId为null
                Integer authorId = blog.getAuthorId();
                if (authorId == null) {
                    continue; // 跳过无效数据
                }
                
                String username = userMapper.getUsernameById(authorId);
                String avatar = userMapper.getAvatarById(authorId);
                
                result.add(new BlogVO(blog, username, avatar));
        }
        return result;
        } catch (Exception e) {
            // 记录异常并返回公开博客
            System.err.println("获取所有博客时发生异常: " + e.getMessage());
            e.printStackTrace();
            return getPublicBlogs();
        }
    }
    
    @Override
    public List<BlogVO> getBlogsByAuthor(int authorId, int requestUserId) {
        List<BlogVO> result = new ArrayList<>();
        List<Blog> blogs;
        
        // 作者本人或管理员可以查看该作者的所有博客
        if (authorId == requestUserId || isAdminUser(requestUserId)) {
            blogs = blogMapper.getBlogsByAuthorId(authorId);
        } else {
            // 筛选出该作者的公开博客
            blogs = blogMapper.getBlogsByAuthorId(authorId);
            blogs.removeIf(blog -> blog.getIsPublic() == 0);
        }
        
        for(Blog blog : blogs) {
            result.add(new BlogVO(blog, userMapper.getUsernameById(blog.getAuthorId()), userMapper.getAvatarById(blog.getAuthorId())));
        }
        return result;
    }

    @Override
    public BlogVO getBlogById(int id, int userId) {
        Blog blog = blogMapper.getBlogById(id);
        
        // 如果博客不存在，返回null
        if (blog == null) {
            return null;
        }
        
        // 检查博客可见性，非公开博客只有作者和管理员可以查看
        if (blog.getIsPublic() == 0 && !Objects.equals(blog.getAuthorId(), userId) && !isAdminUser(userId)) {
            return null;
        }
        
        // 更新浏览量
        blogMapper.updateWatchesById(id);
        
        return new BlogVO(blog, userMapper.getUsernameById(blog.getAuthorId()),
                userMapper.getAvatarById(blog.getAuthorId()),
                likeMapper.checkUserLikedBlog(userId, id) != null,
                false
                );
    }

    @Override
    public String doNewLike(Integer userId, Integer blogId) {
        Blog blog = blogMapper.getBlogById(blogId);
        
        // 检查博客可见性，非公开博客只有作者和管理员可以点赞
        if (blog.getIsPublic() == 0 && !Objects.equals(blog.getAuthorId(), userId) && !isAdminUser(userId)) {
            return "无法对私密博客点赞";
        }
        
        if (likeMapper.checkUserLikedBlog(userId, blogId) != null) return "你已经点过赞了";
        if (likeMapper.addNewLikeRecord(userId, blogId) == 1 && blogMapper.addLikesById(blogId) == 1) {
            String username = userMapper.getUsernameById(userId);
            Notification notification = new Notification(0, "点赞通知", "您的文章:\"" + blog.getTitle() + "\"刚刚被用户" + username + "点赞了", new Date());
            int a = notificationMapper.insertNewNotificationData(notification);
            notificationMapper.insertNewNotificationRS(new NotificationRS(blog.getAuthorId(), notification.getId(), false));
            return null;
        } else return "发生错误，请重试";
    }

    @Override
    public String deleteLike(Integer userId, Integer blogId) {
        if (likeMapper.checkUserLikedBlog(userId, blogId) == null) return "你还没有点过赞";
        if (likeMapper.deleteLikeRecord(userId, blogId) == 1 && blogMapper.unlikeById(blogId) == 1) 
            return null;
        else 
            return "发生错误，请联系管理员";
    }

    @Override
    public int checkUserLikedBlog(Integer userId, Integer blogId) {
        return likeMapper.checkUserLikedBlog(userId, blogId);
    }

    @Override
    public String updateBlog(Integer userId, Integer blogId, String title, String content, Short isPublic) {
        // 检查博客是否存在
        Blog blog = blogMapper.getBlogById(blogId);
        if (blog == null) {
            return "博客不存在";
        }
        
        // 检查用户是否是博客作者
        if (!Objects.equals(blog.getAuthorId(), userId)) {
            return "您没有权限修改此博客";
        }
        
        // 更新博客
        blog.setTitle(title);
        blog.setContent(content);
        blog.setUpdateDate(new Date());
        
        // 如果提供了isPublic参数，则更新
        if (isPublic != null) {
            blog.setIsPublic(isPublic);
        }
        
        if (blogMapper.updateBlog(blog) > 0) {
            return null; // 表示成功
        } else {
            return "更新失败，请稍后重试";
        }
    }

    @Override
    public String deleteBlog(Integer userId, Integer blogId) {
        // 检查博客作者ID
        Integer authorId = blogMapper.getBlogAuthorId(blogId);
        if (authorId == null) {
            return "博客不存在";
        }
        
        // 检查权限（作者或管理员可以删除）
        if (!Objects.equals(authorId, userId) && !isAdminUser(userId)) {
            return "您没有权限删除此博客";
        }
        
        // 删除博客
        if (blogMapper.deleteBlogById(blogId) > 0) {
            return null; // 表示成功
        } else {
            return "删除失败，请稍后重试";
        }
    }
    
    @Override
    public boolean isAdminUser(Integer userId) {
        // 使用User对象直接判断，与UserServiceImpl保持一致
        User user = userMapper.getUserById(userId);
        // 添加更详细的空值检查，避免空指针异常
        System.out.println("检查管理员权限 - userId: " + userId + ", 用户信息: " + (user != null ? "角色: " + (user.getManager() != null && user.getManager() == 1 ? "管理员" : "普通用户") : "用户不存在"));
        
        // 确保user不为null，并且manager不为null且值为1
        return user != null && user.getManager() != null && user.getManager() == 1;
    }
}
