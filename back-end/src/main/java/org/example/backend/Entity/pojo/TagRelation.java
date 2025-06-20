package org.example.backend.Entity.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 标签关联实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TagRelation {
    
    private Integer id;              // 关联ID
    private Integer conversationId;  // 对话ID
    private Integer tagId;           // 标签ID
    
    // 构造函数 - 创建新关联
    public TagRelation(Integer conversationId, Integer tagId) {
        this.conversationId = conversationId;
        this.tagId = tagId;
    }
} 