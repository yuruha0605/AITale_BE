package com.example.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;

// 필터의 역할이 api gateway로 빠짐.
@Configuration

// google auth 관련 설정
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // google auth 관련 설정
    private final OAuth2AuthenticationSuccessHandler authHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 로컬 테스트의 주적, CSRF는 끕니다.
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                // google auth 관련 설정
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(authHandler));
        return http.build();
    }

}