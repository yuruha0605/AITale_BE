package com.aitale.analytics.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "daily_score_stats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DailyScoreStat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false)
  private LocalDate statDate;

  @Column(nullable = false)
  private Integer totalScoreSum;

  @Column(nullable = false)
  private Integer quizCount;

  @Column(nullable = false)
  private Integer correctCount;

  @Column(nullable = false)
  private Integer wrongCount;

  @Column(nullable = false)
  private Double avgScore;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Builder
  public DailyScoreStat(
      Long userId,
      LocalDate statDate,
      Integer totalScoreSum,
      Integer quizCount,
      Integer correctCount,
      Integer wrongCount,
      Double avgScore,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.userId = userId;
    this.statDate = statDate;
    this.totalScoreSum = totalScoreSum;
    this.quizCount = quizCount;
    this.correctCount = correctCount;
    this.wrongCount = wrongCount;
    this.avgScore = avgScore;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public void updateStats(
      Integer totalScoreSum,
      Integer quizCount,
      Integer correctCount,
      Integer wrongCount,
      Double avgScore,
      LocalDateTime updatedAt) {
    this.totalScoreSum = totalScoreSum;
    this.quizCount = quizCount;
    this.correctCount = correctCount;
    this.wrongCount = wrongCount;
    this.avgScore = avgScore;
    this.updatedAt = updatedAt;
  }
}
