package org.example.backend.Entity.pojo;

import lombok.Data;

import java.util.Date;

@Data
public class TalkLike {
    private Integer id, userId, talkId;
    private Date likeTime;

    public TalkLike() {
    }

    public TalkLike(Integer userId, Integer talkId, Date likeTime) {
        this.userId = userId;
        this.talkId = talkId;
        this.likeTime = likeTime;
    }
}