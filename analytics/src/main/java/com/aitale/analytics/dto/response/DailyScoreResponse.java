package com.aitale.analytics.dto.response;

import java.time.LocalDate;
import java.util.List;

public record DailyScoreResponse(Long userId, LocalDate from, LocalDate to,
                                 List<DailyScoreItemResponse> dailyScores) {

}