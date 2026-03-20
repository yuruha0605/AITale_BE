package com.aitale.analytics.dto.response;

public record AccuracyResponse(Long userId, long totalCount, long correctCount, double accuracy) {

}
