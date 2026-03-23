package com.aitale.analytics.presentation;

import com.aitale.analytics.application.AnalyticsService;
import com.aitale.analytics.common.response.ApiResponse;
import com.aitale.analytics.dto.response.AccuracyResponse;
import com.aitale.analytics.dto.response.EventResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Analytics-Service", description = "사용자 분석 및 통계 API")
@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  @Operation(summary = "사용자 이벤트 조회", description = "특정 사용자의 모든 이벤트를 조회합니다")
  @GetMapping("/users/{userId}/events")
  public ApiResponse<List<EventResponse>> getUserEvents(@PathVariable Long userId) {
    return ApiResponse.ok(analyticsService.getUserEvents(userId));
  }

  @Operation(summary = "사용자 정확도 조회", description = "특정 사용자의 추천 정확도를 조회합니다")
  @GetMapping("/users/{userId}/accuracy")
  public ApiResponse<AccuracyResponse> getAccuracy(@PathVariable Long userId) {
    return ApiResponse.ok(analyticsService.getAccuracy(userId));
  }
}
