package com.aitale.recommendation.infrastructure;

import com.aitale.recommendation.dto.ai.AiRecommendationRequest;
import com.aitale.recommendation.dto.ai.AiRecommendationResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ai-service", url = "${AI_SERVICE_URL:${ai.service.url:${service.ai.url:http://host.docker.internal:9000}}}")
public interface AiRecommendationClient {

    @PostMapping("/internal/recommendations")
    AiRecommendationResult recommend(@RequestBody AiRecommendationRequest request);
}