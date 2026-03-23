package com.aitale.recommendation.application;

import com.aitale.recommendation.dto.ai.AiRecommendationRequest;
import com.aitale.recommendation.dto.ai.AiRecommendationResult;
import com.aitale.recommendation.dto.ai.AiStoryCandidate;
import com.aitale.recommendation.dto.response.StoryCandidateResponse;
import com.aitale.recommendation.dto.response.UserProfileResponse;
import com.aitale.recommendation.infrastructure.AiRecommendationClient;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendationAiService {

    private final AiRecommendationClient aiRecommendationClient;

    public AiRecommendationResult recommend(
        UserProfileResponse user,
        List<StoryCandidateResponse> stories,
        int size
    ) {
        AiRecommendationRequest request = new AiRecommendationRequest(
            user.age(),
            user.currentLevel(),
            user.assignedDifficulty(),
            user.interests(),
            size,
            stories.stream()
                .map(story -> new AiStoryCandidate(
                    story.storyId(),
                    story.title(),
                    story.genre(),
                    story.length()
                ))
                .toList()
        );

        return aiRecommendationClient.recommend(request);
    }
}