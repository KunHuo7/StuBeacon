package org.example.backend.Controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.example.backend.Entity.pojo.Blog;
import org.example.backend.Entity.pojo.RestBean;
import org.example.backend.Entity.vo.BlogVO;
import org.example.backend.Service.BlogService;

import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    @Resource
    BlogService blogService;

    @PostMapping("addNew")
    public RestBean<String> addNewBlog(
            HttpServletRequest request,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "isPublic", defaultValue = "1") short isPublic
    ) {
        System.out.println("接收到的isPublic值: " + isPublic);
        Blog blog = new Blog(title, (Integer)request.getAttribute("id"), new Date(), content);
        blog.setIsPublic(isPublic);
        if (blogService.addNewBlog(blog) > 0)
            return RestBean.success("发布成功～");
        else return RestBean.failure(503, "出现错误，请联系管理员");
    }

    @GetMapping("getInroduce")
    public RestBean<List<BlogVO>> getInroduceBlog(HttpServletRequest request){
        try {
        Integer userId = (Integer) request.getAttribute("id");
        // 如果是管理员，返回所有博客，否则只返回公开博客
        if(userId != null) {
            return RestBean.success("获取成功", blogService.getAllBlogs(userId));
        } else {
                return RestBean.success("获取成功", blogService.getPublicBlogs());
            }
        } catch (Exception e) {
            // 打印详细错误信息
            e.printStackTrace();
            // 出现异常时返回空列表，而不是抛出500错误
            return RestBean.success("获取成功", blogService.getPublicBlogs());
        }
    }

    @GetMapping("getBlog")
    public RestBean<BlogVO> getBlog(@RequestParam("id") int id,
                                    HttpServletRequest request){
        Integer userId = (Integer) request.getAttribute("id");
        return RestBean.success("成功",blogService.getBlogById(id,userId));
    }

    @PostMapping("like")
    public RestBean<String> likeBlog(@RequestParam("id") int id,
                                     HttpServletRequest request) {
        int userId = (Integer) request.getAttribute("id");
        String s = blogService.doNewLike(userId, id);
        if (s == null) return RestBean.success("点赞成功！");
        else return RestBean.failure(500, s);
    }

    @PostMapping("unlike")
    public RestBean<String> unlikeBlog(@RequestParam("id") int id,
                                       HttpServletRequest request){
        int userId = (Integer) request.getAttribute("id");
        String s = blogService.deleteLike(userId, id);
        if (s == null) return RestBean.success("取消点赞成功！");
        else return RestBean.failure(500, s);
    }

    @GetMapping("check")
    public RestBean<Integer> checkLikedBlog(@RequestParam("id") int id,
                                            HttpServletRequest request){
        int userId = (Integer) request.getAttribute("id");
        return RestBean.success("",blogService.checkUserLikedBlog(userId,id));
    }
    
    @PostMapping("update")
    public RestBean<String> updateBlog(
            HttpServletRequest request,
            @RequestParam("id") int id,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(value = "isPublic", required = false) Short isPublic
    ) {
        System.out.println("更新博客接收到的isPublic值: " + isPublic);
        Integer userId = (Integer) request.getAttribute("id");
        String s = blogService.updateBlog(userId, id, title, content, isPublic);
        if (s == null) {
            return RestBean.success("更新成功！");
        } else {
            return RestBean.failure(500, s);
        }
    }
    
    @PostMapping("delete")
    public RestBean<String> deleteBlog(
            HttpServletRequest request,
            @RequestParam("id") int id
    ) {
        Integer userId = (Integer) request.getAttribute("id");
        String s = blogService.deleteBlog(userId, id);
        if (s == null) {
            return RestBean.success("删除成功！");
        } else {
            return RestBean.failure(500, s);
        }
    }

    // 管理员获取所有博客，包括私密博客
    @GetMapping("admin/getAllBlogs")
    public RestBean<List<BlogVO>> getAllBlogsForAdmin(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("id");
        
        if (userId == null) {
            return RestBean.failure(401, "请先登录");
        }
        
        return RestBean.success("获取成功", blogService.getAllBlogs(userId));
    }
}