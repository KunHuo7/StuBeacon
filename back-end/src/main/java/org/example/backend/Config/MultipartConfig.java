package org.example.backend.Config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import jakarta.servlet.MultipartConfigElement;

@Configuration
public class MultipartConfig {

    /**
     * 文件上传配置
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        // 单个文件最大10MB
        factory.setMaxFileSize(DataSize.ofMegabytes(10));
        // 设置总上传数据总大小15MB
        factory.setMaxRequestSize(DataSize.ofMegabytes(15));
        return factory.createMultipartConfig();
    }

    /**
     * 配置multipart解析器
     * @return StandardServletMultipartResolver
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
} 