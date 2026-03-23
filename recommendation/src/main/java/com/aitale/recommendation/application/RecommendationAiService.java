package com.aitale.recommendation.application;

import com.aitale.aiservice.application.RecommendationPromptService;
import com.aitale.recommendation.dto.ai.AiRecommendRequest;
import com.aitale.recommendation.dto.ai.AiRecommendationResult;
import com.aitale.recommendation.dto.response.StoryCandidateResponse;
import com.aitale.recommendation.dto.response.UserProfileResponse;
import com.aitale.recommendation.infrastructure.AiRecommendationClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationAiService {

    private final RecommendationPromptService recommendationPromptService;
    private final AiRecommendationClient aiRecommendationClient;

    public AiRecommendationResult recommend(
        UserProfileResponse user,
        List<StoryCandidateResponse> stories,
        int size
    ) {
        String prompt = recommendationPromptService.createPrompt(user, stories, size);
        return aiRecommendationClient.recommend(new AiRecommendRequest(prompt));
    }
}