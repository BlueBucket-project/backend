package com.example.shopping.config.connect;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 리액트와 연동해서 협을 할 때
// 이 설정을 하지 않으면 CORS에러가 발생합니다.
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 리액트는 기본적으로 3000번이지만
                // 협업하는 프론트에서 5173으로 바꿔서 5173으로 해놓음
                .allowedOrigins("http://localhost:5173",
                        "http://blue-bucket-front.s3-website.ap-northeast-2.amazonaws.com/")
                .allowedHeaders("Authorization", "Content-Type")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
    }
}
