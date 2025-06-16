package org.example.backend.Entity.pojo;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Data
public class User {
    private Integer id;
//    private Integer managerId;
    private String  username,password,mail,avatar;
    private String studentId;
    private Integer manager;
    private Integer unReadNotiCnt;
    // 用户状态：0-待审核，1-已审核通过，2-已拒绝
    private Integer status;

    public User(){
        // 默认为待审核状态
        this.status = 0;
    }

}
