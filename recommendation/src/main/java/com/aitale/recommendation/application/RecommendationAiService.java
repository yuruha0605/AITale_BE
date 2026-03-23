package com.aitale.recommendation.application;

import com.aitale.recommendation.dto.ai.AiRecommendationResult;
import com.aitale.recommendation.dto.ai.AiRecommendedStory;
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

//    public AiRecommendationResult recommend(
//        UserProfileResponse user,
//        List<StoryCandidateResponse> stories,
//        int size
//    ) {
//        String prompt = recommendationPromptService.createPrompt(user, stories, size);
//        return aiRecommendationClient.recommend(prompt);
//    }

    // TODO: 테스트 성공 시 삭제
    public AiRecommendationResult recommend(
        UserProfileResponse user,
        List<StoryCandidateResponse> stories,
        int size
    ) {
        return new AiRecommendationResult(
            List.of(
                new AiRecommendedStory(201L, 1, "관심사와 난이도에 적합한 동화입니다."),
                new AiRecommendedStory(202L, 2, "사용자 수준에 맞는 동화입니다."),
                new AiRecommendedStory(203L, 3, "흥미를 가질 만한 모험 이야기입니다.")
            )
        );
    }
}