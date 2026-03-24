package com.aitale.analytics.dto.response;

import java.time.LocalDate;

public record DailyScoreItemResponse(LocalDate statDate, Integer totalScoreSum, Integer quizCount,
                                     Integer correctCount, Integer wrongCount, Double avgScore) {

}