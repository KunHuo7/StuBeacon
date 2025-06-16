package org.example.backend.Entity.vo;

import lombok.Data;
import org.example.backend.Entity.pojo.TalkComment;

@Data
public class TalkCommentVO {
    private TalkComment comment;
    private String author;
    private String avatar;
    private Boolean isLiked;
    
    public TalkCommentVO(TalkComment comment, String author, String avatar, Boolean isLiked) {
        this.comment = comment;
        this.author = author;
        this.avatar = avatar;
        this.isLiked = isLiked;
    }
    
    public TalkCommentVO(TalkComment comment, String author, String avatar) {
        this.comment = comment;
        this.author = author;
        this.avatar = avatar;
    }

    public TalkCommentVO() {

    }
}