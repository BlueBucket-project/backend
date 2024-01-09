package com.example.shopping.config.connect;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/*
 *   writer : YuYoHan
 *   work :
 *          React와 SpringBoot와 서버가 다르기 때문에 연결하는 역할을 하고 있습니다.
 *          프론트가 배포된 백엔드 코드로 개발할 수 있게 로컬과 프론트가 s3에 배포했을 때
 *          배포 주소를 연결하고 있습니다. 이 설정을 하지 않으면 CORS 에러가 발생합니다.
 *   date : 2023/12/04
 * */

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
