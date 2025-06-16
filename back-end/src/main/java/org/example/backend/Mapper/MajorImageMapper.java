package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.MajorImage;

import java.util.List;

/**
 * 专业图片Mapper接口
 */
@Mapper
public interface MajorImageMapper {
    
    /**
     * 查询所有专业图片
     */
    @Select("SELECT id, major_id as majorId, image_type as imageType, image_url as imageUrl, sort_order as sortOrder, description FROM major_image ORDER BY sort_order ASC")
    List<MajorImage> findAll();
    
    /**
     * 根据ID查询专业图片
     */
    @Select("SELECT id, major_id as majorId, image_type as imageType, image_url as imageUrl, sort_order as sortOrder, description FROM major_image WHERE id = #{id}")
    MajorImage findById(Integer id);
    
    /**
     * 根据专业ID查询专业图片
     */
    @Select("SELECT id, major_id as majorId, image_type as imageType, image_url as imageUrl, sort_order as sortOrder, description FROM major_image WHERE major_id = #{majorId} ORDER BY sort_order ASC")
    List<MajorImage> findByMajorId(Integer majorId);
    
    /**
     * 根据专业ID和图片类型查询专业图片
     */
    @Select("SELECT id, major_id as majorId, image_type as imageType, image_url as imageUrl, sort_order as sortOrder, description FROM major_image WHERE major_id = #{majorId} AND image_type = #{imageType} ORDER BY sort_order ASC")
    List<MajorImage> findByMajorIdAndType(Integer majorId, String imageType);
    
    /**
     * 添加专业图片
     */
    @Insert("INSERT INTO major_image(major_id, image_type, image_url, sort_order, description) " +
            "VALUES(#{majorId}, #{imageType}, #{imageUrl}, #{sortOrder}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(MajorImage majorImage);
    
    /**
     * 更新专业图片
     */
    @Update("UPDATE major_image SET major_id = #{majorId}, image_type = #{imageType}, " +
            "image_url = #{imageUrl}, sort_order = #{sortOrder}, description = #{description} " +
            "WHERE id = #{id}")
    int update(MajorImage majorImage);
    
    /**
     * 删除专业图片
     */
    @Delete("DELETE FROM major_image WHERE id = #{id}")
    int deleteById(Integer id);
    
    /**
     * 根据专业ID删除专业图片
     */
    @Delete("DELETE FROM major_image WHERE major_id = #{majorId}")
    int deleteByMajorId(Integer majorId);
    
    /**
     * 根据专业ID和图片类型删除专业图片
     */
    @Delete("DELETE FROM major_image WHERE major_id = #{majorId} AND image_type = #{imageType}")
    int deleteByMajorIdAndType(Integer majorId, String imageType);
} 