package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.Course;

import java.util.List;

/**
 * 课程Mapper接口
 */
@Mapper
public interface CourseMapper {
    
    /**
     * 查询所有课程
     */
    @Select("SELECT * FROM course")
    List<Course> findAll();
    
    /**
     * 根据ID查询课程
     */
    @Select("SELECT * FROM course WHERE id = #{id}")
    Course findById(Integer id);
    
    /**
     * 根据专业ID查询课程列表
     */
    @Select("SELECT * FROM course WHERE major_id = #{majorId}")
    List<Course> findByMajorId(Integer majorId);
    
    /**
     * 根据专业ID和课程分类查询课程
     */
    @Select("SELECT * FROM course WHERE major_id = #{majorId} AND category = #{category}")
    List<Course> findByMajorIdAndCategory(Integer majorId, String category);
    
    /**
     * 添加课程
     */
    @Insert("INSERT INTO course(name, major_id, description, credits, category, prerequisites) " +
            "VALUES(#{name}, #{majorId}, #{description}, #{credits}, #{category}, #{prerequisites})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Course course);
    
    /**
     * 更新课程
     */
    @Update("UPDATE course SET name = #{name}, major_id = #{majorId}, description = #{description}, " +
            "credits = #{credits}, category = #{category}, prerequisites = #{prerequisites} " +
            "WHERE id = #{id}")
    int update(Course course);
    
    /**
     * 删除课程
     */
    @Delete("DELETE FROM course WHERE id = #{id}")
    int deleteById(Integer id);
} 