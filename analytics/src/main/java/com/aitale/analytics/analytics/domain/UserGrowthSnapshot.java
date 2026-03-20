package com.aitale.analytics.analytics.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_growth_snapshot")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGrowthSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private LocalDate snapshotDate;

    @Column(nullable = false)
    private Integer currentLevel;

    @Column(nullable = false)
    private Integer totalScore;

    @Column(nullable = false)
    private Integer totalQuizCount;

    @Column(nullable = false)
    private Integer totalCorrectCount;

    // 전체 정답률
    @Column(nullable = false)
    private Double overallCorrectRate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public UserGrowthSnapshot(Long userId, LocalDate snapshotDate, Integer currentLevel,
        Integer totalScore, Integer totalQuizCount,
        Integer totalCorrectCount, Double overallCorrectRate,
        LocalDateTime createdAt) {
        this.userId = userId;
        this.snapshotDate = snapshotDate;
        this.currentLevel = currentLevel;
        this.totalScore = totalScore;
        this.totalQuizCount = totalQuizCount;
        this.totalCorrectCount = totalCorrectCount;
        this.overallCorrectRate = overallCorrectRate;
        this.createdAt = createdAt;
    }

    public void updateSnapshot(Integer currentLevel, Integer totalScore, Integer totalQuizCount,
        Integer totalCorrectCount, Double overallCorrectRate) {
        this.currentLevel = currentLevel;
        this.totalScore = totalScore;
        this.totalQuizCount = totalQuizCount;
        this.totalCorrectCount = totalCorrectCount;
        this.overallCorrectRate = overallCorrectRate;
    }
}