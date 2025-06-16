package org.example.backend.Entity.pojo;

import lombok.Data;
import java.util.Date;

@Data
public class NotificationQueryObject {
    private Integer id, type, notificationId;
    private Boolean isRead;
    private String title, content;
    private Date createTime;

    public NotificationQueryObject() {

    }
}
