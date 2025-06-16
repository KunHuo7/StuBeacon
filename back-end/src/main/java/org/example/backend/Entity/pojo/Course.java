package org.example.backend.Entity.pojo;

import lombok.Data;

/**
 * 课程实体类
 */
@Data
public class Course {
    private Integer id;          // 课程ID
    private String name;         // 课程名称
    private Integer majorId;     // 所属专业ID
    private String description;  // 课程描述
    private Integer credits;     // 学分
    private String category;     // 课程分类（必修/选修）
    private String prerequisites; // 先修课程
} 