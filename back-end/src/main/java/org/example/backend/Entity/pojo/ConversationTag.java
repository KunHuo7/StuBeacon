package org.example.backend.Entity.pojo;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * AI对话标签实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationTag {
    
    private Integer id;        // 标签ID
    private String name;       // 标签名称
    private Integer userId;    // 创建用户ID
    private Date createdAt;    // 创建时间
    
    // 构造函数 - 创建新标签
    public ConversationTag(String name, Integer userId) {
        this.name = name;
        this.userId = userId;
        this.createdAt = new Date();
    }
} 