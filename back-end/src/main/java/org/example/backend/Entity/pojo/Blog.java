package org.example.backend.Entity.pojo;

import lombok.Data;

import java.util.Date;
@Data

public class Blog {
    private Integer id,authorId,watches,likes,favourites;
    private  String title,content;
    private Date updateDate;
    private short isPublic; // 1-公开 0-私密
    public Blog(){
        this.isPublic = 1; // 默认为公开
    }


    public Blog( String title,Integer authorId, Date updateDate,String content) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.updateDate = updateDate;
        this.isPublic = 1; // 默认为公开
    }
    
    public Blog( String title,Integer authorId, Date updateDate,String content, short isPublic) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.updateDate = updateDate;
        this.isPublic = isPublic;
    }
}
