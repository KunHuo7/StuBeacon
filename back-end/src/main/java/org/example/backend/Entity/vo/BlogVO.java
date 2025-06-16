package org.example.backend.Entity.vo;

import lombok.Data;
import org.example.backend.Entity.pojo.Blog;

//View Object
@Data
public class BlogVO {
    private Blog blog;
    private String username,avatar;
    private Boolean isLiked,isFav;

    public BlogVO(Blog blog, String username, String avatar, Boolean isLiked, Boolean isFav) {
        this.blog = blog;
        this.username = username;
        this.avatar = avatar;
        this.isLiked = isLiked;
        this.isFav = isFav;
    }

    public BlogVO(Blog blog, String username, String avatar) {
        this.blog = blog;
        this.username = username;
        this.avatar = avatar;
    }
}
