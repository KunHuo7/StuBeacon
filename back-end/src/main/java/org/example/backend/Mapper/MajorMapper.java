package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.Major;

import java.util.List;

/**
 * 专业Mapper接口
 */
@Mapper
public interface MajorMapper {
    
    /**
     * 查询所有专业
     */
    @Select("SELECT id, name, college_id as collegeId, credit_requirements as creditRequirements, main_courses as mainCourses FROM major")
    List<Major> findAll();
    
    /**
     * 根据ID查询专业
     */
    @Select("SELECT id, name, college_id as collegeId, credit_requirements as creditRequirements, main_courses as mainCourses FROM major WHERE id = #{id}")
    Major findById(Integer id);
    
    /**
     * 根据学院ID查询专业列表
     */
    @Select("SELECT id, name, college_id as collegeId, credit_requirements as creditRequirements, main_courses as mainCourses FROM major WHERE college_id = #{collegeId}")
    List<Major> findByCollegeId(Integer collegeId);
    
    /**
     * 添加专业
     */
    @Insert("INSERT INTO major(name, college_id, credit_requirements, main_courses) " +
            "VALUES(#{name}, #{collegeId}, #{creditRequirements}, #{mainCourses})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Major major);
    
    /**
     * 更新专业
     */
    @Update("UPDATE major SET name = #{name}, college_id = #{collegeId}, " +
            "credit_requirements = #{creditRequirements}, main_courses = #{mainCourses} " +
            "WHERE id = #{id}")
    int update(Major major);
    
    /**
     * 删除专业
     */
    @Delete("DELETE FROM major WHERE id = #{id}")
    int deleteById(Integer id);
} 