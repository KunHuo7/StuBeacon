package org.example.backend.Entity.pojo;

import lombok.Data;

/**
 * 专业图片实体类
 */
@Data
public class MajorImage {
    private Integer id;          // 图片ID
    private Integer majorId;     // 所属专业ID
    private String imageType;    // 图片类型：CREDIT_REQUIREMENT(学分要求)、MAIN_COURSE(主要课程)、STRUCTURE(课程关系图)
    private String imageUrl;     // 图片URL
    private Integer sortOrder;   // 排序顺序
    private String description;  // 图片描述
} 