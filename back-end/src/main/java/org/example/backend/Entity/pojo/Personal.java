package org.example.backend.Entity.pojo;

import lombok.Data;
import jakarta.validation.constraints.*;
import java.util.Date;

@Data
public class Personal {
    private Integer userid;
    
    @Min(value = 0, message = "性别值不正确")
    @Max(value = 2, message = "性别值不正确")
    private Integer sex;
    
    @Min(value = 0, message = "年龄不能小于0")
    @Max(value = 150, message = "年龄不能大于150")
    private Integer age;
    
    @Size(max = 500, message = "爱好长度不能超过500个字符")
    private String hobby;
    
    @Size(max = 100, message = "专业长度不能超过100个字符")
    private String major;
    
    @Size(max = 50, message = "学生类型长度不能超过50个字符")
    private String type;
    
    @Size(max = 1000, message = "个人简介长度不能超过1000个字符")
    private String introduction;
    
    @Size(max = 100, message = "联系方式长度不能超过100个字符")
    private String contact;
    
    private Date createTime;
    private Date updateTime;

    public Personal() {
    }

    public Personal(Integer userid, Integer sex, Integer age, String hobby, 
                   String major, String type, String introduction, 
                   String contact) {
        this.userid = userid;
        this.sex = sex;
        this.age = age;
        this.hobby = hobby;
        this.major = major;
        this.type = type;
        this.introduction = introduction;
        this.contact = contact;
        this.createTime = new Date();
        this.updateTime = new Date();
    }
}
