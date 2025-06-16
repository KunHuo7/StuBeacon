package org.example.backend.Service.Impl;

import jakarta.annotation.Resource;
import org.example.backend.Entity.pojo.Personal;
import org.example.backend.Mapper.PersonalMapper;
import org.example.backend.Service.PersonalService;
import org.springframework.stereotype.Service;

@Service
public class PersonalServiceImpl implements PersonalService {

    @Resource
    private PersonalMapper personalMapper;
    
    @Override
    public boolean addPersonal(Personal personalInfo) {
        // 检查用户是否已存在个人信息
        if (personalMapper.checkPersonalExists(personalInfo.getUserid()) > 0) {
            // 如果已存在，则更新信息（确保只存储最后一次更新的数据）
            return updatePersonal(personalInfo);
        }
        // 不存在则添加新记录
        return personalMapper.insertPersonal(personalInfo) > 0;
    }
    
    @Override
    public boolean updatePersonal(Personal personalInfo) {
        return personalMapper.updatePersonal(personalInfo) > 0;
    }
    
    @Override
    public boolean deletePersonal(Integer userid) {
        return personalMapper.deletePersonal(userid) > 0;
    }
    
    @Override
    public Personal getPersonal(Integer userid) {
        return personalMapper.selectPersonalByUserId(userid);
    }
}
