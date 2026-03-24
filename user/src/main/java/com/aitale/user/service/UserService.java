package com.aitale.user.service;

import com.aitale.user.domain.dto.request.LoginRequestDTO;
import com.aitale.user.domain.dto.request.UserInterestRequestDTO;
import com.aitale.user.domain.dto.request.UserRequestDTO;
import com.aitale.user.domain.dto.response.UserResponseDTO;
import com.aitale.user.domain.entity.UserEntity;
import com.aitale.user.domain.entity.UserInterestEntity;
import com.aitale.user.provider.JwtProvider;
import com.aitale.user.repository.UserInterestRepository;
import com.aitale.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserInterestRepository userInterestRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public UserResponseDTO signUp(UserRequestDTO userRequestDTO) {
        // 1. DTO를 Entity로 변환 (비밀번호 암호화 포함)

        UserEntity userEntity = UserEntity.builder()
            .email(userRequestDTO.getEmail())
            .password(passwordEncoder.encode(userRequestDTO.getPassword())) // 암호화 후 주입
            .age(userRequestDTO.getAge())
            .currentLevel(1)
            .build();

        // 2. DB 저장 (save 메서드는 Entity를 인자로 받습니다)
        UserEntity savedUser = userRepository.save(userEntity);

        // 3. ResponseDTO 생성 및 반환
        // 생성자나 빌더를 사용하여 필요한 정보를 채워줍니다.
        return UserResponseDTO.builder()
            .email(savedUser.getEmail())
            .age(savedUser.getAge())
            .currentLevel(savedUser.getCurrentLevel())
            .assignedDifficulty(savedUser.getAssignedDifficulty())
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
        String at = jwtProvider.createAT(entity.getUserSystemId());
        String rt = jwtProvider.createRT(entity.getUserSystemId());

        System.out.println(">>>> 3. user service RT토큰 Redis 저장");
        redisTemplate.opsForValue()
            .set("RT:" + entity.getUserSystemId(), rt, REFRESH_TOKEN_TTL, TimeUnit.SECONDS);

        map.put("access", at);
        map.put("refresh", rt);

        return map;
    }

    // 난이도 배정
    public String createAssignedDifficulty(Long userSystemId, String difficulty) {

        UserEntity userEntity = userRepository.findById(userSystemId)
            .orElseThrow(() -> new RuntimeException("Not Found!!"));
        userEntity.assignDifficulty(difficulty);

        userRepository.save(userEntity);
        return difficulty;

    }

    // 프로필 (이메일)불러오기
    public String getEmail(Long userSystemId) {

        UserEntity userEntity = userRepository.findById(userSystemId)
            .orElseThrow(() -> new RuntimeException("Not Found!!"));
        return userEntity.getEmail();

    }

    // 프로필 (이메일, 나이, 레벨, 난이도) 불러오기
    public UserResponseDTO getProfile(Long userSystemId) {

        UserEntity userEntity = userRepository.findById(userSystemId)
            .orElseThrow(() -> new RuntimeException("Not Found!!"));

        return UserResponseDTO.builder()
            .email(userEntity.getEmail())
            .age(userEntity.getAge())
            .currentLevel(userEntity.getCurrentLevel())
            .assignedDifficulty(userEntity.getAssignedDifficulty())
            .build();

    }

    // 관심사 설정
    public List<Long> createInterest(Long userSystemId, UserInterestRequestDTO requestDTO) {

        UserEntity userEntity = userRepository.findById(userSystemId)
            .orElseThrow(() -> new RuntimeException("Not Found!!"));

        List<Long> interestResponseList = new ArrayList<>();
        for (Long interest : requestDTO.getInterests()) {
            UserInterestEntity entity = UserInterestEntity.builder()
                .userSystemId(userEntity.getUserSystemId())
                .interestId(interest)
                .build();

            UserInterestEntity savedInterest = userInterestRepository.save(entity);
            interestResponseList.add(savedInterest.getInterestId());
        }
        return interestResponseList;

    }

    // 관심사 불러오기
    public List<Long> getInterests(Long userSystemId) {

        List<UserInterestEntity> entities = userInterestRepository.findByUserSystemId(userSystemId);

        if (entities.isEmpty()) {
            throw new RuntimeException("Not Found!!");
        }

        List<Long> interests = new ArrayList<>();

        for (UserInterestEntity entity : entities) {
            interests.add(entity.getInterestId());
        }

        return interests;
    }

    public int increaseUserLevel(Long userSystemId) {

        UserEntity userEntity = userRepository.findById(userSystemId)
            .orElseThrow(() -> new RuntimeException("Not Found!!"));
        int updateLevel = userEntity.getCurrentLevel() + 1;
        userEntity.changeLevel(updateLevel);

        return updateLevel;

    }

}
