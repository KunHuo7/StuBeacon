package org.example.backend.Entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class TalkComment {
    private Integer id, authorId, talkId, likes, parentId;
    private String content;
    private Date createTime;
    
    public TalkComment() {}
    
    public TalkComment(Integer authorId, Integer talkId, String content, Date createTime) {
        this.authorId = authorId;
        this.talkId = talkId;
        this.content = content;
        this.createTime = createTime;
    }
    
    public TalkComment(Integer authorId, Integer talkId, String content, Date createTime, Integer parentId) {
        this.authorId = authorId;
        this.talkId = talkId;
        this.content = content;
        this.createTime = createTime;
        this.parentId = parentId;
    }
}