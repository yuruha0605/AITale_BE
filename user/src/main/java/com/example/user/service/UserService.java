package com.example.user.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.user.domain.dto.LoginRequestDTO;
import com.example.user.domain.dto.UserRequestDTO;
import com.example.user.domain.dto.UserResponseDTO;
import com.example.user.domain.entity.UserEntity;
import com.example.user.provider.JwtProvider;
import com.example.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserResponseDTO signUp(UserRequestDTO userRequestDTO) {
        // 1. DTO를 Entity로 변환 (비밀번호 암호화 포함)

        UserEntity userEntity = UserEntity.builder()
                .email(userRequestDTO.getEmail())
                .password(passwordEncoder.encode(userRequestDTO.getPassword())) // 암호화 후 주입
                .age(userRequestDTO.getAge())
                .build();

        // 2. DB 저장 (save 메서드는 Entity를 인자로 받습니다)
        UserEntity savedUser = userRepository.save(userEntity);

        // 3. ResponseDTO 생성 및 반환
        // 생성자나 빌더를 사용하여 필요한 정보를 채워줍니다.
        return UserResponseDTO.builder()
                .email(savedUser.getEmail())
                .build();
    }

    // redis
    @Qualifier("tokenRedis")
    private final long REFRESH_TOKEN_TTL = 60 * 60 * 24 * 7;
    private final RedisTemplate<String, Object> redisTemplate;

    public Map<String, Object> signIn(LoginRequestDTO request) {
        System.out.println(">>>> user service signin");
        Map<String, Object> map = new HashMap<>();

        System.out.println(">>>> 1. user service 사용자 조회");

        // hashing version
        UserEntity entity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Not Found!!"));

        // (plain vs encoded)
        if (!passwordEncoder.matches(request.getPassword(), entity.getPassword())) {
            throw new RuntimeException("Password Not Matched");
        }

        System.out.println(">>>> 2. user service 토큰 생성");
        String at = jwtProvider.createAT(entity.getEmail());
        String rt = jwtProvider.createRT(entity.getEmail());

        System.out.println(">>>> 3. user service RT토큰 Redis 저장");
        redisTemplate.opsForValue()
                .set("RT:" + entity.getEmail(), rt, REFRESH_TOKEN_TTL, TimeUnit.SECONDS);

        map.put("access", at);
        map.put("refresh", rt);

        return map;
    }

    // 난이도 배정 기능은 추후 비즈니스 로직 연결 예정
    public void assignedDifficulty(int score) {
        // TODO: score 기반 난이도 계산 및 사용자 프로필 반영
    }

    // 프로필(이메일) 불러오기

    // 관심사 관리

}
