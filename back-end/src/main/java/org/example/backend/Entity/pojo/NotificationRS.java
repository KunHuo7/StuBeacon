package org.example.backend.Entity.pojo;

import lombok.Data;

@Data

public class NotificationRS {
    private Integer id,userId,notiId;
    private boolean isRead;
    public NotificationRS(){

    }

    public NotificationRS( Integer userId, Integer notiId,boolean isRead) {
        this.userId = userId;
        this.notiId = notiId;
        this.isRead=  isRead;
    }
}
