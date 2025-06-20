package org.example.backend.Entity.pojo;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * AI对话实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {
    
    private Integer id;          // 对话ID
    private String title;        // 对话标题
    // 摘要字段已从数据库表中移除
    private transient String summary = null;  // 摘要功能已禁用
    private Integer userId;      // 用户ID
    private Date createdAt;      // 创建时间
    private Date updatedAt;      // 最后更新时间
    private String lastMessage;  // 最后一条消息内容（用于预览，截断）
    private Integer totalMessages; // 消息总数
    
    // 构造函数 - 创建新对话
    public Conversation(String title, Integer userId, Date createdAt) {
        this.title = title;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = createdAt;
        this.totalMessages = 0;
    }
} 