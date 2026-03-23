package com.aitale.recommendation.infrastructure;

import com.aitale.recommendation.dto.ai.AiRecommendationResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "ai-service")
public interface AiRecommendationClient {

    @PostMapping("/internal/recommend")
    AiRecommendationResult recommend(String prompt);
}