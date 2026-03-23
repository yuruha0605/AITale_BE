package com.aitale.recommendation.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "recommendation_request_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendationRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String interestSummary;

    private Integer level;

    private String difficulty;

    private Integer recommendedCount;

    private LocalDateTime generatedAt;

    @Enumerated(EnumType.STRING)
    private RecommendationStatus status;

    @Builder
    public RecommendationRequestLog(
        Long userId,
        String interestSummary,
        Integer level,
        String difficulty,
        Integer recommendedCount,
        LocalDateTime generatedAt,
        RecommendationStatus status
    ) {
        this.userId = userId;
        this.interestSummary = interestSummary;
        this.level = level;
        this.difficulty = difficulty;
        this.recommendedCount = recommendedCount;
        this.generatedAt = generatedAt;
        this.status = status;
    }
}