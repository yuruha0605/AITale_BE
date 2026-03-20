package com.aitale.analytics.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record StudyResultCreateRequest(@NotBlank String studyResultId, @NotNull Long userId,
                                       @NotNull Long storyId, @NotBlank String genre,
                                       @NotNull LocalDateTime solvedAt,
                                       @NotNull @Min(0) Integer totalScore,
                                       @NotNull @Min(0) Integer quizCount,
                                       @NotNull @Min(0) Integer correctCount,
                                       @NotNull @Min(0) Integer wrongCount,
                                       @NotNull @Min(1) Integer currentLevel) {

    public boolean hasInvalidCounts() {
        return correctCount + wrongCount != quizCount;
    }
}