package com.example.user.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private static final long ACCESS_TOKEN_EXPIRY = 1000L * 60 * 30;
    private static final long REFRESH_TOKEN_EXPIRY = 1000L * 60 * 60 * 24 * 7;

    @PostConstruct
    public void validateSecret() {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("jwt.secret must be at least 32 bytes");
        }
    }

    private Key getStringKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // access token provider
    public String createAT(Long userSystemId) {
        System.out.println(">>>> Provider createAT : " + userSystemId);

        return Jwts.builder()
            // Subject에 식별자인 ID를 넣음
            .setSubject(String.valueOf(userSystemId))
            .setIssuedAt(new Date())
            // 30분 만료
            .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRY))
            .signWith(getStringKey())
            .compact();
    }
    // public String createAT(String email) {
    // System.out.println(">>>> Provider createAT : "+email);
    // return Jwts.builder()
    // .setSubject(email)
    // .setIssuedAt(new Date())
    // .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30 ))
    // .signWith(getStringKey())
    // .compact() ;
    // }

    // refresh token provider
    public String createRT(Long userSystemId) {
        System.out.println(">>>> Provider createRT : " + userSystemId);
        return Jwts.builder()
            .setSubject(String.valueOf(userSystemId)) // ID를 String으로 변환하여 저장
            .setIssuedAt(new Date())
            // 7일 만료
            .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRY))
            .signWith(getStringKey())
            .compact();
    }
    // public String createRT(String email) {
    // System.out.println(">>>> Provider createRT : "+email);
    // return Jwts.builder()
    // .setSubject(email)
    // .setIssuedAt(new Date())
    // .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 7
    // ))
    // .signWith(getStringKey())
    // .compact() ;
    // }

    // token 에서 subject 추출
    // Bearer xxxxxxx
    // 메서드명을 목적에 맞게 변경: getUserEmail... -> getUserId...
    public Long getUserIdFromToken(String token) {
        System.out.println(">>>> Provider getUserIdFromToken");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            Claims claims = Jwts.parserBuilder() // 최신 버전 라이브러리라면 parserBuilder 권장
                .setSigningKey(getStringKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

            // Subject에 담긴 String 기반 ID를 Long으로 다시 변환
            return Long.parseLong(claims.getSubject());
        } catch (Exception e) {
            // 토큰이 만료되었거나 변조되었을 때의 예외 처리 로직 필요
            System.err.println("Token parsing error: " + e.getMessage());
            return null;
        }
    }
    // public String getUserEmailFromToken(String token) {
    // System.out.println(">>>> Provider getUserEmailFromToken token : "+token);
    // if(token.startsWith("Bearer ")) {
    // token = token.substring(7) ;
    // }
    // Claims claims = Jwts.parser()
    // .setSigningKey(getStringKey())
    // .parseClaimsJws(token)
    // .getBody() ;

    // return claims.getSubject() ;
    // }

    public long getATE() {
        return ACCESS_TOKEN_EXPIRY;
    }

    public long getRTE() {
        return REFRESH_TOKEN_EXPIRY;
    }

}