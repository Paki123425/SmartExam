package com.smartexam.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 仅允许前端开发端口
                .allowedOrigins("http://localhost:5173")
                // 兼容浏览器预检请求
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                // 前后端不需要 Cookie 的话更安全
                .allowCredentials(false)
                .maxAge(3600);
    }
}

