package com.aitale.ai.presentation;

import com.aitale.ai.application.OpenAiRecommendationService;
import com.aitale.ai.dto.AiRecommendationRequest;
import com.aitale.ai.dto.AiRecommendationResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AI-Service", description = "AI 기반 추천 및 분석 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class AiRecommendationController {

    private final OpenAiRecommendationService openAiRecommendationService;

    @Operation(summary = "AI 추천 생성", description = "OpenAI를 기반으로 개인화된 추천을 생성합니다")
    @PostMapping("/recommendations")
    public AiRecommendationResult recommend(
            @Valid @RequestBody AiRecommendationRequest request) {
        return openAiRecommendationService.recommend(request);
    }
}