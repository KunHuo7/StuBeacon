package org.example.backend.Entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Notification {
    private Integer id,type;
    private String title,content;
    Date createTime;

    public Notification(){

    }

    public Notification(Integer type, String title, String content, Date createTime) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
    }
}


