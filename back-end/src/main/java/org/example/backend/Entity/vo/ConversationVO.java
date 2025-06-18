package org.example.backend.Entity.vo;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 对话视图对象，用于前端显示
 */
@Data
public class ConversationVO {
    private String id;          // 对话ID (String类型以匹配前端)
    private String title;       // 对话标题
    private String summary;     // 对话总结/摘要
    private String lastMessage; // 最后一条消息预览
    private Date date;          // 更新日期
    private Integer totalMessages; // 消息总数
    private List<String> tags;  // 标签列表
} 