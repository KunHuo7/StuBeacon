package org.example.backend.Entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class Talk {
    private Integer id, authorId, views, likes, comments;
    private String title, content;
    private Date createTime;
    private String[] tags;
    
    public Talk() {}
    
    public Talk(String title, Integer authorId, String content, Date createTime) {
        this.title = title;
        this.authorId = authorId;
        this.content = content;
        this.createTime = createTime;
        this.views = 0;
        this.likes = 0;
        this.comments = 0;
    }
}