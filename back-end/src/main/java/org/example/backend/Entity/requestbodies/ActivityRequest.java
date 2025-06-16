package org.example.backend.Entity.requestbodies;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Data
public class ActivityRequest {
    private Integer id;
    private String activityName;
    private String location;
    private String organizer;
    private Date startTime;
    private Date endTime;
    private String image;
    private Integer maxParticipants;
    private String description;
} 