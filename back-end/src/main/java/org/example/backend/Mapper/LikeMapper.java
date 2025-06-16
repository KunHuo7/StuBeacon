package org.example.backend.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper

public interface LikeMapper {
   @Insert("INSERT INTO rp_like(userId,blogId,likeTime) VALUES (#{userId},#{blogId},CURRENT_TIMESTAMP)")
    int addNewLikeRecord(Integer userId,Integer blogId);

   @Select("SELECT id FROM rp_like WHERE userId = #{userId} AND blogId = #{blogId}")
    Integer checkUserLikedBlog(Integer userId,Integer blogId);

    @Delete("DELETE FROM rp_like WHERE userId = #{userId} AND blogId = #{blogId}")
    int deleteLikeRecord(Integer userId, Integer blogId);
}
