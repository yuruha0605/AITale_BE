package com.aitale.analytics.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "genre_performance_stats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GenrePerformanceStat {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long userId;

  @Column(nullable = false, length = 50)
  private String genre;

  // 해당 장르 푼 횟수
  @Column(nullable = false)
  private Integer attemptCount;

  // 장르별 정답률
  @Column(nullable = false)
  private Double correctRate;

  // 장르별 평균 점수
  @Column(nullable = false)
  private Double avgScore;

  // 마지막 풀이 시각
  @Column(nullable = false)
  private LocalDateTime lastSolvedAt;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Builder
  public GenrePerformanceStat(
      Long userId,
      String genre,
      Integer attemptCount,
      Double correctRate,
      Double avgScore,
      LocalDateTime lastSolvedAt,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.userId = userId;
    this.genre = genre;
    this.attemptCount = attemptCount;
    this.correctRate = correctRate;
    this.avgScore = avgScore;
    this.lastSolvedAt = lastSolvedAt;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public void updateStats(
      Integer attemptCount,
      Double correctRate,
      Double avgScore,
      LocalDateTime lastSolvedAt,
      LocalDateTime updatedAt) {
    this.attemptCount = attemptCount;
    this.correctRate = correctRate;
    this.avgScore = avgScore;
    this.lastSolvedAt = lastSolvedAt;
    this.updatedAt = updatedAt;
  }
}
