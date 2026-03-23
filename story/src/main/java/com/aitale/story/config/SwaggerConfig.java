package com.aitale.story.config;

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
                .addServersItem(new Server().url("/story-service").description("Gateway route base path"))
                .info(new Info()
                        .title("Story Service API 명세서")
                        .description("MSA 구조에서 스토리 도메인을 담당하는 API")
                        .version("1.0.0"));
    }
}
