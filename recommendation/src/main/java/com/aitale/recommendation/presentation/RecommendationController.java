package com.aitale.recommendation.presentation;

import com.aitale.recommendation.application.RecommendationCommandService;
import com.aitale.recommendation.application.RecommendationService;
import com.aitale.recommendation.common.response.ApiResponse;
import com.aitale.recommendation.dto.request.RecommendationGenerateRequest;
import com.aitale.recommendation.dto.response.RecommendationGenerateResponse;
import com.aitale.recommendation.dto.response.RecommendationListResponse;
import com.aitale.recommendation.dto.response.RecommendationLogResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Recommendation-Service", description = "AI 기반 추천 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;
    private final RecommendationCommandService recommendationCommandService;

    @Operation(summary = "사용자 추천 목록 조회", description = "특정 사용자의 추천 스토리 목록을 조회합니다")
    @GetMapping("/users/{userId}")
    public ApiResponse<RecommendationListResponse> getRecommendations(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "false") boolean refresh) {
        return ApiResponse.ok(recommendationService.getRecommendations(userId, size, refresh));
    }

    @Operation(summary = "추천 생성", description = "AI 모델을 통해 새로운 추천을 생성합니다")
    @PostMapping("/users/{userId}/generate")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<RecommendationGenerateResponse> generateRecommendations(
            @PathVariable Long userId,
            @RequestBody(required = false) RecommendationGenerateRequest request) {
        Integer size = request == null ? 5 : request.size();
        return ApiResponse.ok(
                recommendationCommandService.generateRecommendations(userId, size),
                "추천 생성이 완료되었습니다.");
    }

    @Operation(summary = "추천 로그 조회", description = "사용자의 추천 로그 히스토리를 조회합니다")
    @GetMapping("/users/{userId}/logs")
    public ApiResponse<Page<RecommendationLogResponse>> getRecommendationLogs(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ApiResponse.ok(recommendationService.getRecommendationLogs(userId, page, size));
    }

    @Operation(summary = "추천 캐시 삭제", description = "특정 사용자의 추천 캐시를 삭제합니다")
    @DeleteMapping("/users/{userId}")
    public ApiResponse<Void> deleteRecommendationCache(@PathVariable Long userId) {
        recommendationCommandService.deleteRecommendationCache(userId);
        return ApiResponse.ok(null, "추천 캐시 삭제가 완료되었습니다.");
    }
}