package org.example.backend.Service;

import org.example.backend.XunFei.BigModel;

/**
 * AI服务接口
 */
public interface AIService {
    
    /**
     * 获取AI模型服务实例
     * 
     * @param userId 用户ID，用于标识对话历史
     * @return BigModel 服务实例
     */
    BigModel getAIModel(String userId);
    
    /**
     * 发送消息到AI模型并获取回复
     *
     * @param userId 用户ID
     * @param message 用户消息
     * @return AI回复内容
     */
    String chat(String userId, String message) throws Exception;
    
    /**
     * 清除用户的对话历史
     * 
     * @param userId 用户ID
     */
    void clearHistory(String userId);
} 