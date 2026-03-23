package com.aitale.recommendation.infrastructure;

import com.aitale.recommendation.dto.ai.AiRecommendationRequest;
import com.aitale.recommendation.dto.ai.AiRecommendationResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service", url = "${ai.service.url:http://localhost:9000}")
public interface AiRecommendationClient {

    @PostMapping("/internal/recommendations")
    AiRecommendationResult recommend(@RequestBody AiRecommendationRequest request);
}