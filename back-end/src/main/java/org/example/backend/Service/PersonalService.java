package org.example.backend.Service;

import org.example.backend.Entity.pojo.Personal;

public interface PersonalService {
    /**
     * 添加学生个人信息
     * @param personalInfo 学生个人信息对象
     * @return 操作结果
     */
    boolean addPersonal(Personal personalInfo);
    
    /**
     * 更新学生个人信息
     * @param personalInfo 学生个人信息对象
     * @return 操作结果
     */
    boolean updatePersonal(Personal personalInfo);
    
    /**
     * 删除学生个人信息
     * @param userid 学生ID
     * @return 操作结果
     */
    boolean deletePersonal(Integer userid);
    
    /**
     * 查询学生个人信息
     * @param userid 学生ID
     * @return 学生个人信息对象
     */
    Personal getPersonal(Integer userid);
}
