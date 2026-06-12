package com.aivle.bookapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 API 주소에 대해 CORS 허용
                .allowedOrigins(
                        "http://localhost:3000", // 로컬 Next.js 개발 서버
                        "https://library-aivle-5th.onrender.com" // Render 프론트엔드 주소
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false); // 인증 정보 없이 요청 허용 (쿠키/세션 등 제외)
    }
}
