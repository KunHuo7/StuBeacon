package org.example.backend.Mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.backend.Entity.pojo.Personal;

@Mapper
public interface PersonalMapper {

    @Insert("INSERT INTO tb_personal (userid, sex, age, hobby, major, type, " +
            "introduction, contact, create_time, update_time) " +
            "VALUES (#{userid}, #{sex}, #{age}, #{hobby}, #{major}, #{type}, " +
            "#{introduction}, #{contact}, #{createTime}, #{updateTime})")
    int insertPersonal(Personal personalInfo);
    
    @Select("SELECT * FROM tb_personal WHERE userid = #{userid}")
    Personal selectPersonalByUserId(Integer userid);
    
    @Update("UPDATE tb_personal SET sex = #{sex}, age = #{age}, hobby = #{hobby}, " +
            "major = #{major}, type = #{type}, " +
            "introduction = #{introduction}, contact = #{contact}, update_time = #{updateTime} " +
            "WHERE userid = #{userid}")
    int updatePersonal(Personal personalInfo);
    
    @Delete("DELETE FROM tb_personal WHERE userid = #{userid}")
    int deletePersonal(Integer userid);
    
    @Select("SELECT COUNT(*) FROM tb_personal WHERE userid = #{userid}")
    int checkPersonalExists(Integer userid);
}
