package com.aitale.recommendation.application;

import com.aitale.recommendation.dto.response.StoryCandidateResponse;
import com.aitale.recommendation.dto.response.UserProfileResponse;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RecommendationPromptService {

    public String createPrompt(
        UserProfileResponse user,
        List<StoryCandidateResponse> stories,
        int size
    ) {
        StringBuilder builder = new StringBuilder();

        builder.append("당신은 아동 독해력 학습 서비스의 추천 시스템입니다.\n");
        builder.append("사용자 정보를 바탕으로 가장 적절한 동화 ")
            .append(size)
            .append("개를 추천하세요.\n\n");

        builder.append("[사용자 정보]\n");
        builder.append("- userId: ").append(user.userId()).append("\n");
        builder.append("- age: ").append(user.age()).append("\n");
        builder.append("- level: ").append(user.currentLevel()).append("\n");
        builder.append("- difficulty: ").append(user.assignedDifficulty()).append("\n");
        builder.append("- interests: ").append(user.interests()).append("\n\n");

        builder.append("[후보 동화 목록]\n");
        for (StoryCandidateResponse story : stories) {
            builder.append("- storyId: ").append(story.storyId())
                .append(", title: ").append(story.title())
                .append(", genre: ").append(story.genre())
                .append(", length: ").append(story.length())
                .append("\n");
        }

        builder.append("\n[규칙]\n");
        builder.append("1. 나이, 레벨, 관심사, 난이도를 모두 고려하세요.\n");
        builder.append("2. 가장 적절한 순서대로 추천하세요.\n");
        builder.append("3. 각 추천마다 짧은 이유를 작성하세요.\n");
        builder.append("4. 반드시 아래 JSON 배열 형식으로만 답하세요.\n\n");

        builder.append("""
            [
              {
                "storyId": 201,
                "rankOrder": 1,
                "reason": "관심사와 난이도에 적합한 동화입니다."
              }
            ]
            """);

        return builder.toString();
    }
}