package com.aitale.analytics.analytics.dto.request;

import java.time.LocalDateTime;

public record StudyResultCreateRequest(Long userId, Long storyId, String genre,
                                       LocalDateTime solvedAt, Integer totalScore,
                                       Integer quizCount, Integer correctCount, Integer wrongCount,
                                       Integer currentLevel) {

}