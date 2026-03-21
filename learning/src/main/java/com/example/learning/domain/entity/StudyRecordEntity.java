package com.example.learning.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "study_record_tbl")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordId;

    @Column(nullable = false, name = "user_id")
    private Long userId;

    @Column(nullable = false, name = "story_id")
    private Long storyId;

    @Column(name = "base_correct_count")
    private int baseCorrectCount;

    @Column(name = "bonus_correct_count")
    private int bonusCorrectCount;

    @Column(name = "is_bonus_triggered")
    private boolean isBonusTriggered;

    @Column(name = "is_highest_difficulty")
    private boolean isHighestDifficulty;

    @Column(name = "total_score")
    private int totalScore;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;
}