package org.example.backend.Entity.pojo;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class CampusActivity {
    private Integer id;
    private String activityName;
    private String location;
    private String organizer;
    private Date startTime;
    private Date endTime;
    private String image;
    private Integer maxParticipants;
    private Integer currentParticipants;
    private String description;
    private Integer userId;  // 发布者ID
    private Date createTime;
    private Integer status;  // 0:待审核 1:已通过 2:已拒绝 3:用户已删除
    
    public CampusActivity() {
    
    }

    public CampusActivity(Integer id,String activityName,String location,String organizer,Date startTime,
                          Date endTime,String image,Integer maxParticipants,Integer currentParticipants,String description,Integer userId,Date createTime,Integer status) {
        this.id = id;
        this.activityName = activityName;
        this.location = location;
        this.organizer = organizer;
        this.startTime = startTime;
        this.endTime = endTime;
        this.image = image;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.description = description;
        this.userId = userId;
        this.createTime = createTime;
        this.status = status;
    }
} 