package com.example.apigateway.config;

import org.springframework.context.annotation.Configuration;

/**
 * CORS 설정은 application.yml의 spring.cloud.gateway.globalcors에서 관리합니다.
 * 이 클래스는 사용하지 않습니다.
 */
@Configuration
public class CorsConfig {
    // CORS 설정이 중복되지 않도록 이 클래스는 비활성화합니다.
    // gateway의 globalcors 설정만 사용합니다.
}

