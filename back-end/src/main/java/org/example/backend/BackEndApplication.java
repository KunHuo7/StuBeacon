package org.example.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
@ServletComponentScan //扫描所有包中的Servlet组件 拦截器 过滤器 Servlet
public class BackEndApplication {

    @Value("${file.upload-dir}")
    private String uploadDir;

    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

    @Bean
    CommandLineRunner init() {
        return args -> {
            // 确保上传目录在应用启动时存在
            File uploadDirectory = new File(uploadDir);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
                System.out.println("已创建图片上传目录: " + uploadDirectory.getAbsolutePath());
            }
        };
    }
}
