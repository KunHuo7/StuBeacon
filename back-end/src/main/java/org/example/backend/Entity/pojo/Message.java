package org.example.backend.Entity.pojo;

import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * AI对话消息实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    
    private Integer id;             // 消息ID
    private Integer conversationId; // 所属对话ID
    private String role;            // 角色（user/assistant）
    private String content;         // 消息内容
    private Date timestamp;         // 时间戳
    
    // 构造函数 - 创建新消息
    public Message(Integer conversationId, String role, String content, Date timestamp) {
        this.conversationId = conversationId;
        this.role = role;
        this.content = content;
        this.timestamp = timestamp;
    }
} 