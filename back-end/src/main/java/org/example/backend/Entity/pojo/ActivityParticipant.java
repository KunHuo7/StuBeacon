package org.example.backend.Entity.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class ActivityParticipant {
    private Integer id;
    private Integer activityId;
    private Integer userId;
    private Date joinTime;
    
    public ActivityParticipant() {
    
    }
} 