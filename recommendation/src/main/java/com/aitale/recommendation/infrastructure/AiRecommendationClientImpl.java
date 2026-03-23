package com.aitale.recommendation.infrastructure;

import com.aitale.recommendation.dto.ai.AiRecommendationResult;
import com.aitale.recommendation.dto.ai.AiRecommendedStory;
import java.util.List;
import org.springframework.stereotype.Component;

// TODO: 테스트 성공 시 클래스 삭제
@Component
public class AiRecommendationClientImpl implements AiRecommendationClient {

    @Override
    public AiRecommendationResult recommend(String prompt) {
        return new AiRecommendationResult(
            List.of(
                new AiRecommendedStory(201L, 1, "관심사와 난이도에 적합한 동화입니다."),
                new AiRecommendedStory(202L, 2, "사용자 수준에 맞는 동화입니다."),
                new AiRecommendedStory(203L, 3, "흥미를 가질 만한 모험 이야기입니다.")
            )
        );
    }
}