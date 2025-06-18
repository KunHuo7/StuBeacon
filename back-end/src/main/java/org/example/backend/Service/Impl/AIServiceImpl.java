package org.example.backend.Service.Impl;

import org.example.backend.Service.AIService;
import org.example.backend.XunFei.BigModel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AI服务实现类
 */
@Service
public class AIServiceImpl implements AIService {
    
    // 用户ID -> BigModel实例的映射，用于管理不同用户的对话实例
    private final Map<String, BigModel> userModels = new ConcurrentHashMap<>();
    
    @Override
    public BigModel getAIModel(String userId) {
        // 如果用户没有对应的模型实例，创建一个新的
        return userModels.computeIfAbsent(userId, id -> new BigModel(id));
    }
    
    @Override
    public String chat(String userId, String message) throws Exception {
        // 获取用户的AI模型实例
        BigModel model = getAIModel(userId);
        
        // 调用大模型进行对话
        return model.chat(message);
    }
    
    @Override
    public void clearHistory(String userId) {
        // 获取用户的AI模型实例
        BigModel model = userModels.get(userId);
        
        // 如果存在模型实例，清除历史记录
        if (model != null) {
            model.clearHistory();
        }
    }
} 