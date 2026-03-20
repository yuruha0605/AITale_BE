package com.aitale.analytics.analytics.presentation;

import com.aitale.analytics.analytics.application.AnalyticsService;
import com.aitale.analytics.analytics.dto.request.EventCreateRequest;
import com.aitale.analytics.analytics.dto.response.EventResponse;
import com.aitale.analytics.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytics/internal")
@RequiredArgsConstructor
public class InternalAnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/events")
    public ApiResponse<EventResponse> saveEvent(@RequestBody EventCreateRequest request) {
        return ApiResponse.ok(analyticsService.saveEvent(request), "이벤트가 저장되었습니다.");
    }
}
