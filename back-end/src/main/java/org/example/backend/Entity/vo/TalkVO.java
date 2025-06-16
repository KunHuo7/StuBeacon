package org.example.backend.Entity.vo;

import lombok.Data;
import org.example.backend.Entity.pojo.Talk;

@Data
public class TalkVO {
    private Talk talk;
    private String username, avatar;
    private Boolean isLiked;

    public TalkVO(Talk talk, String username, String avatar, Boolean isLiked) {
        this.talk = talk;
        this.username = username;
        this.avatar = avatar;
        this.isLiked = isLiked;
    }

    public TalkVO(Talk talk, String username, String avatar) {
        this.talk = talk;
        this.username = username;
        this.avatar = avatar;
        this.isLiked = false;
    }

    public TalkVO() {
        this.isLiked = false;
    }
}