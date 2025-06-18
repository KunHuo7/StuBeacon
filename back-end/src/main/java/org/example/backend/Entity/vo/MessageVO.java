package org.example.backend.Entity.vo;

import lombok.Data;
import java.util.Date;

/**
 * 消息视图对象，用于前端显示
 */
@Data
public class MessageVO {
    private String id;       // 消息ID (String类型以匹配前端)
    private String role;     // 角色（user/assistant）
    private String content;  // 消息内容
    private Date timestamp;  // 时间戳
} 