package com.aitale.recommendation.presentation;

import com.aitale.recommendation.application.RecommendationCommandService;
import com.aitale.recommendation.common.response.ApiResponse;
import com.aitale.recommendation.dto.request.InternalRecommendationBuildRequest;
import com.aitale.recommendation.dto.response.RecommendationGenerateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendations/internal")
public class InternalRecommendationController {

    private final RecommendationCommandService recommendationCommandService;

    @PostMapping("/build")
    public ApiResponse<RecommendationGenerateResponse> buildRecommendation(
        @Valid @RequestBody InternalRecommendationBuildRequest request
    ) {
        return ApiResponse.ok(
            recommendationCommandService.buildInternal(request),
            "내부 추천 생성이 완료되었습니다."
        );
    }
}