package org.example.backend.Entity.pojo;

import lombok.Data;

/**
 * 专业实体类
 */
@Data
public class Major {
    private Integer id;                // 专业ID
    private String name;               // 专业名称
    private Integer collegeId;         // 所属学院ID
    private String creditRequirements; // 学分要求文本说明
    private String mainCourses;        // 主要课程文本说明
    // 不再使用单一图片路径
    // private String structureImage;  // 课程关系图路径
} 