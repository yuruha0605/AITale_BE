package com.example.user.config;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.user.domain.entity.UserEntity;
import com.example.user.provider.JwtProvider;
import com.example.user.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

        private final JwtProvider jwtProvider;
        private final UserRepository userRepository;

        // 추가
        private final long REFRESH_TOKEN_TTL = 60 * 60 * 24 * 7;
        private final RedisTemplate<String, Object> redisTemplate;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException {

                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                // 소셜 서비스마다 이메일을 꺼내는 키가 다를 수 있으니 주의 (구글은 "email")
                String email = oAuth2User.getAttribute("email");

                // 1. DB 확인 및 가입 (네가 작성한 로직 그대로)
                UserEntity userEntity = userRepository.findByEmail(email)
                                .orElseGet(() -> {
                                        UserEntity newUser = UserEntity.builder()
                                                        .email(email)
                                                        .age(0)
                                                        .currentLevel(1)
                                                        .build();
                                        return userRepository.save(newUser);
                                });

                // 2. 토큰 생성
                String at = jwtProvider.createAT(userEntity.getUserSystemId());
                String rt = jwtProvider.createRT(userEntity.getUserSystemId());

                // 3. Redis 저장
                System.out.println(">>>> Social login RT Redis 저장");
                redisTemplate.opsForValue()
                                .set("RT:" + userEntity.getEmail(), rt, REFRESH_TOKEN_TTL, TimeUnit.SECONDS);

                // 4. 리다이렉트 처리 (Map을 리턴하는 대신 URL에 담아서 보냄)
                // 로컬 테스트용 타겟 URL (프론트 메인 url)
                String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:3000")
                                .queryParam("accessToken", at)
                                .queryParam("refreshToken", rt)
                                .build().toUriString();

                getRedirectStrategy().sendRedirect(request, response, targetUrl);
        }
}