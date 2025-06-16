package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.College;

import java.util.List;

/**
 * 学院Mapper接口
 */
@Mapper
public interface CollegeMapper {
    
    /**
     * 查询所有学院
     */
    @Select("SELECT id, name FROM college")
    List<College> findAll();
    
    /**
     * 根据ID查询学院
     */
    @Select("SELECT id, name FROM college WHERE id = #{id}")
    College findById(Integer id);
    
    /**
     * 添加学院
     */
    @Insert("INSERT INTO college(name) VALUES(#{name})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(College college);
    
    /**
     * 更新学院
     */
    @Update("UPDATE college SET name = #{name} WHERE id = #{id}")
    int update(College college);
    
    /**
     * 删除学院
     */
    @Delete("DELETE FROM college WHERE id = #{id}")
    int deleteById(Integer id);
} 