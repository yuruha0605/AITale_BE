package com.aitale.aiservice.application;

import com.aitale.aiservice.dto.AiRecommendationRequest;
import com.aitale.aiservice.dto.AiStoryCandidate;
import org.springframework.stereotype.Service;

@Service
public class RecommendationPromptService {

    public String createPrompt(AiRecommendationRequest request) {
        StringBuilder builder = new StringBuilder();

        builder.append("당신은 아동 독해력 학습 서비스의 추천 시스템입니다.\n");
        builder.append("사용자 정보를 바탕으로 가장 적절한 동화 ")
            .append(request.size())
            .append("개를 추천하세요.\n\n");

        builder.append("[사용자 정보]\n");
        builder.append("- age: ").append(request.age()).append("\n");
        builder.append("- level: ").append(request.level()).append("\n");
        builder.append("- difficulty: ").append(request.difficulty()).append("\n");
        builder.append("- interests: ")
            .append(String.join(", ", request.interests()))
            .append("\n\n");

        builder.append("[후보 동화 목록]\n");
        for (AiStoryCandidate story : request.stories()) {
            builder.append("- storyId: ").append(story.storyId())
                .append(", title: ").append(story.title())
                .append(", genre: ").append(story.genre())
                .append(", length: ").append(story.length())
                .append("\n");
        }

        builder.append("\n[규칙]\n");
        builder.append("1. 나이, 레벨, 관심사, 난이도를 모두 고려하세요.\n");
        builder.append("2. 반드시 후보 동화 목록에 있는 storyId만 사용하세요.\n");
        builder.append("3. 가장 적절한 순서대로 ").append(request.size()).append("개를 추천하세요.\n");
        builder.append("4. rankOrder는 1부터 시작하는 연속된 숫자로 작성하세요.\n");
        builder.append("5. 각 추천마다 한글로 짧은 이유를 작성하세요.\n");
        builder.append("6. 설명, 마크다운, 코드블록 없이 JSON만 출력하세요.\n");
        builder.append("7. 반드시 아래 JSON 객체 형식으로만 답하세요.\n\n");

        builder.append("""
            {
              "recommendations": [
                {
                  "storyId": 201,
                  "rankOrder": 1,
                  "reason": "관심사와 난이도에 적합한 동화입니다."
                }
              ]
            }
            """);

        return builder.toString();
    }
}