package org.example.backend.Service;

import org.example.backend.Entity.pojo.Blog;
import org.example.backend.Entity.vo.BlogVO;

import java.util.List;

public interface BlogService {
   int addNewBlog(Blog blog);
   List<BlogVO> getPublicBlogs();
   List<BlogVO> getAllBlogs(int userId);
   List<BlogVO> getBlogsByAuthor(int authorId, int requestUserId);
   BlogVO getBlogById(int id, int userId);

   String doNewLike(Integer userId, Integer blogId);

   String deleteLike(Integer userId, Integer blogId);

   int checkUserLikedBlog(Integer userId, Integer blogId);
   
   String updateBlog(Integer userId, Integer blogId, String title, String content, Short isPublic);
   
   String deleteBlog(Integer userId, Integer blogId);
   
   boolean isAdminUser(Integer userId);
}
