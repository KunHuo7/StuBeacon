package org.example.backend.Entity.pojo;

import lombok.Data;

@Data
public class LikeRecord {
    private Integer id,userId,bolgId;
    private Data likeTime;

    public LikeRecord(){

    }
}
