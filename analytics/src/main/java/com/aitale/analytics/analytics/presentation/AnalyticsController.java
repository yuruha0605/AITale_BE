package com.aitale.analytics.analytics.presentation;

import com.aitale.analytics.analytics.application.AnalyticsService;
import com.aitale.analytics.analytics.dto.response.AccuracyResponse;
import com.aitale.analytics.analytics.dto.response.EventResponse;
import com.aitale.analytics.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping("/users/{userId}/events")
    public ApiResponse<List<EventResponse>> getUserEvents(@PathVariable Long userId) {
        return ApiResponse.ok(analyticsService.getUserEvents(userId));
    }

    @GetMapping("/users/{userId}/accuracy")
    public ApiResponse<AccuracyResponse> getAccuracy(@PathVariable Long userId) {
        return ApiResponse.ok(analyticsService.getAccuracy(userId));
    }
}