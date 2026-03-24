package com.aitale.recommendation.application;

import com.aitale.recommendation.domain.Recommendation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationCacheService {

    private static final Duration TTL = Duration.ofHours(6);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    public List<Recommendation> get(Long userId) {
        String value = stringRedisTemplate.opsForValue().get(generateKey(userId));
        if (value == null) {
            return List.of();
        }

        try {
            return objectMapper.readValue(value, new TypeReference<>() {
            });
        } catch (JsonProcessingException exception) {
            return List.of();
        }
    }

    public void put(Long userId, List<Recommendation> recommendations) {
        try {
            String value = objectMapper.writeValueAsString(recommendations);
            stringRedisTemplate.opsForValue().set(generateKey(userId), value, TTL);
        } catch (JsonProcessingException ignored) {
        }
    }

    public void evict(Long userId) {
        stringRedisTemplate.delete(generateKey(userId));
    }

    private String generateKey(Long userId) {
        return "recommendation:user:" + userId;
    }
}