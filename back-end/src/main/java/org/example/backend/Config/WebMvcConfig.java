package org.example.backend.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 确保uploadDir目录存在
        File uploadDirectory = new File(uploadDir);
        if (!uploadDirectory.exists()) {
            uploadDirectory.mkdirs();
        }
        
        // 确保course_structure子目录存在
        File courseStructureDir = new File(uploadDirectory, "course_structure");
        if (!courseStructureDir.exists()) {
            courseStructureDir.mkdirs();
        }
        
        // 将 URL 路径 /uploads/** 映射到文件系统的 uploadDir 目录
        // 确保路径以斜杠结尾
        String resourceLocation = "file:" + (uploadDir.endsWith("/") || uploadDir.endsWith("\\") ? uploadDir : uploadDir + "/");
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(resourceLocation);
        
        // 默认的静态资源映射
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
} 