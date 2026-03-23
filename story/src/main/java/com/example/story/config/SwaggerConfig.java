package com.example.story.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Story Service API 명세서")
                        .description("MSA 구조에서 스토리 도메인을 담당하는 API")
                        .version("1.0.0"));
    }
}
