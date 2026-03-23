package com.aitale.analytics.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_growth_snapshot", uniqueConstraints = {
    @UniqueConstraint(name = "uk_user_growth_snapshot_user_date", columnNames = { "user_id", "snapshot_date" })
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGrowthSnapshot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "snapshot_date", nullable = false)
  private LocalDate snapshotDate;

  @Column(nullable = false)
  private Integer currentLevel;

  @Column(nullable = false)
  private Integer totalScore;

  @Column(nullable = false)
  private Integer totalQuizCount;

  @Column(nullable = false)
  private Integer totalCorrectCount;

  @Column(nullable = false)
  private Double overallCorrectRate;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  @Version
  private Long version;

  @Builder
  public UserGrowthSnapshot(
      Long userId,
      LocalDate snapshotDate,
      Integer currentLevel,
      Integer totalScore,
      Integer totalQuizCount,
      Integer totalCorrectCount,
      Double overallCorrectRate,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this.userId = userId;
    this.snapshotDate = snapshotDate;
    this.currentLevel = currentLevel;
    this.totalScore = totalScore;
    this.totalQuizCount = totalQuizCount;
    this.totalCorrectCount = totalCorrectCount;
    this.overallCorrectRate = overallCorrectRate;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public void updateSnapshot(
      Integer currentLevel,
      Integer totalScore,
      Integer totalQuizCount,
      Integer totalCorrectCount,
      Double overallCorrectRate,
      LocalDateTime updatedAt) {
    this.currentLevel = currentLevel;
    this.totalScore = totalScore;
    this.totalQuizCount = totalQuizCount;
    this.totalCorrectCount = totalCorrectCount;
    this.overallCorrectRate = overallCorrectRate;
    this.updatedAt = updatedAt;
  }

  public void applyDelta(int scoreDelta, int quizDelta, int correctDelta, LocalDateTime updatedAt) {
    this.totalScore += scoreDelta;
    this.totalQuizCount += quizDelta;
    this.totalCorrectCount += correctDelta;
    this.overallCorrectRate = this.totalQuizCount == 0 ? 0.0 : (double) this.totalCorrectCount / this.totalQuizCount;
    this.updatedAt = updatedAt;
  }
}
