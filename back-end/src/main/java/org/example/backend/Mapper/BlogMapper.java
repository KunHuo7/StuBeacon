package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.Blog;

import java.util.List;


@Mapper
public interface BlogMapper {
    @Insert("INSERT INTO tb_blogs(title,authorId,updateDate,content,isPublic)VALUES (#{title},#{authorId},#{updateDate},#{content},#{isPublic})")
    @Options(useGeneratedKeys = true,keyProperty = "id")
    int insertNewBlog(Blog blog);

    @Select("SELECT id,title,substring(content,1,40) AS content,authorId,isPublic FROM tb_blogs WHERE isPublic=1 ORDER BY id DESC LIMIT 50")
    List<Blog> getPublicBlogsOrderById();
    
    @Select("SELECT id,title,substring(content,1,40) AS content,authorId,isPublic FROM tb_blogs ORDER BY id DESC LIMIT 50")
    List<Blog> getAllBlogsOrderById();
    
    @Select("SELECT id,title,substring(content,1,40) AS content,authorId,isPublic FROM tb_blogs WHERE authorId=#{authorId} ORDER BY id DESC")
    List<Blog> getBlogsByAuthorId(int authorId);

    @Select("SELECT * FROM tb_blogs WHERE id=#{id}")
    Blog getBlogById(int id);

    @Update("UPDATE tb_blogs SET watches = watches + 1 WHERE id=#{id}")
    int updateWatchesById(int id);

    @Update("UPDATE tb_blogs SET likes = likes + 1 WHERE id=#{id}")
    int addLikesById(int id);

    @Update("UPDATE tb_blogs SET likes = likes - 1 WHERE id=#{id}")
    int unlikeById(int id);

    @Update("UPDATE tb_blogs SET title = #{title}, content = #{content}, updateDate = #{updateDate}, isPublic = #{isPublic} WHERE id = #{id}")
    int updateBlog(Blog blog);

    @Delete("DELETE FROM tb_blogs WHERE id = #{id}")
    int deleteBlogById(int id);
    
    @Select("SELECT authorId FROM tb_blogs WHERE id = #{id}")
    Integer getBlogAuthorId(int id);
}
