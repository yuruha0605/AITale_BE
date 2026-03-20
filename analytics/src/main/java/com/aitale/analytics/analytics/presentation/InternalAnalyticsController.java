package com.aitale.analytics.analytics.presentation;

import com.aitale.analytics.analytics.application.AnalyticsCommandService;
import com.aitale.analytics.analytics.application.AnalyticsService;
import com.aitale.analytics.analytics.dto.request.EventCreateRequest;
import com.aitale.analytics.analytics.dto.request.StudyResultCreateRequest;
import com.aitale.analytics.analytics.dto.response.EventResponse;
import com.aitale.analytics.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytics/internal")
@RequiredArgsConstructor
public class InternalAnalyticsController {

    private final AnalyticsService analyticsService;
    private final AnalyticsCommandService analyticsCommandService;

    @PostMapping("/events")
    public ApiResponse<EventResponse> saveEvent(@RequestBody EventCreateRequest request) {
        return ApiResponse.ok(analyticsService.saveEvent(request), "이벤트가 저장되었습니다.");
    }

    @PostMapping("/study-results")
    public ApiResponse<Void> saveStudyResult(@RequestBody StudyResultCreateRequest request) {
        analyticsCommandService.saveStudyResult(request);
        return ApiResponse.ok(null, "학습 결과가 반영되었습니다.");
    }
}
