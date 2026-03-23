package com.aitale.recommendation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addServersItem(new Server().url("/recommendation-service").description("Gateway route base path"))
                .info(new Info()
                        .title("Recommendation Service API 명세서")
                        .description("AI 기반 스토리 추천 관련 API")
                        .version("1.0.0"));
    }
}
