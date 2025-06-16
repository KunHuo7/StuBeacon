package org.example.backend.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.backend.Entity.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM tb_users WHERE username = #{username} AND password = #{password}")
    User getUserByUsernameAndPassword(String username,String password);

    @Select("SELECT username FROM tb_users WHERE id=#{id}")
    String getUsernameById(Integer id);

    @Select("SELECT * FROM tb_users WHERE id = #{id}")
    User getUserById(Integer id);

    @Select("SELECT avatar FROM  tb_users WHERE id=#{id}")
    String getAvatarById(Integer id);

    //注册
    @Select("SELECT * FROM tb_users WHERE username = #{username}")
    User getUserByUsername(String username);

    @Insert("INSERT INTO tb_users (username, password, manager) VALUES (#{username}, #{password}, #{manager})")
    boolean createUser(String username, String password, Integer manager);
    
    // 创建用户（完整信息）- 默认状态为待审核
    @Insert("INSERT INTO tb_users (username, password, mail, studentId, manager, status) " +
            "VALUES (#{user.username}, #{user.password}, #{user.mail}, #{user.studentId}, #{user.manager}, #{user.status})")
    int createUserComplete(@Param("user") User user);
    
    // 创建待审核用户（注册用）
    @Insert("INSERT INTO tb_users (username, password, mail, studentId, manager, status) " +
            "VALUES (#{username}, #{password}, #{mail}, #{studentId}, 0, 0)")
    int registerUser(String username, String password, String mail, String studentId);

    @Update("UPDATE tb_users SET avatar = #{avatar} WHERE id = #{id}")
    int updateAvatar(@Param("id") Integer id, @Param("avatar") String avatar);

    @Update("UPDATE tb_users SET manager = #{manager} WHERE id = #{id}")
    int updateManager(@Param("id") Integer id, @Param("manager") Integer manager);
    
    // 获取所有用户
    @Select("SELECT * FROM tb_users")
    List<User> getAllUsers();
    
    // 按用户名模糊搜索用户
    @Select("SELECT * FROM tb_users WHERE username LIKE CONCAT('%', #{keyword}, '%')")
    List<User> searchUsersByUsername(@Param("keyword") String keyword);
    
    // 删除用户
    @Delete("DELETE FROM tb_users WHERE id = #{id}")
    int deleteUser(@Param("id") Integer id);
    
    // 更新用户信息
    @Update("UPDATE tb_users SET username = #{user.username}, mail = #{user.mail}, " +
            "avatar = #{user.avatar}, studentId = #{user.studentId}, manager = #{user.manager} " +
            "WHERE id = #{user.id}")
    int updateUser(@Param("user") User user);
    
    // 更新用户密码
    @Update("UPDATE tb_users SET password = #{password} WHERE id = #{id}")
    int updatePassword(@Param("id") Integer id, @Param("password") String password);
    
    // 按邮箱查询用户
    @Select("SELECT * FROM tb_users WHERE mail = #{mail}")
    User getUserByMail(@Param("mail") String mail);
    
    // 按学号查询用户
    @Select("SELECT * FROM tb_users WHERE studentId = #{studentId}")
    User getUserByStudentId(@Param("studentId") String studentId);
    
    // =========================== 用户审核相关 ============================
    
    // 获取待审核用户列表
    @Select("SELECT * FROM tb_users WHERE status = 0")
    List<User> getPendingUsers();
    
    // 更新用户审核状态
    @Update("UPDATE tb_users SET status = #{status} WHERE id = #{id}")
    int updateUserStatus(@Param("id") Integer id, @Param("status") Integer status);
    
    // 检查用户是否已通过审核
    @Select("SELECT status FROM tb_users WHERE username = #{username}")
    Integer getUserStatus(String username);
    
    // 修改后的登录方法，增加状态检查
    @Select("SELECT * FROM tb_users WHERE username = #{username} AND password = #{password} AND status = 1")
    User getApprovedUserByUsernameAndPassword(String username, String password);

    // 获取用户角色信息（admin或user）
    @Select("SELECT CASE WHEN manager = 1 THEN 'admin' ELSE 'user' END FROM tb_users WHERE id = #{id}")
    String getUserRoleById(Integer id);
}
