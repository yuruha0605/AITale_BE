package com.aitale.analytics.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .addServersItem(
            new Server().url("/analytics-service").description("Gateway route base path"))
        .info(
            new Info()
                .title("Analytics Service API 명세서")
                .description("데이터 분석 및 통계 API")
                .version("1.0.0"))
        .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
        .components(
            new io.swagger.v3.oas.models.Components()
                .addSecuritySchemes(
                    "Bearer Authentication",
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("JWT Bearer Token")));
  }
}
