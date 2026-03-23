package com.aitale.recommendation.infrastructure;

import com.aitale.recommendation.dto.ai.AiRecommendationResult;

public interface AiRecommendationClient {

    AiRecommendationResult recommend(String prompt);
}