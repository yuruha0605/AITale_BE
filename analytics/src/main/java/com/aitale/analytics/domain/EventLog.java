package com.aitale.analytics.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
    name = "event_log",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_event_log_study_result_id", columnNames = "study_result_id")
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "study_result_id", unique = true, length = 100)
    private String studyResultId;

    @Column(nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EventType eventType;

    private Long targetId;

    private Boolean correct;

    private Integer score;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public EventLog(
        String studyResultId,
        Long userId,
        EventType eventType,
        Long targetId,
        Boolean correct,
        Integer score,
        LocalDateTime createdAt
    ) {
        this.studyResultId = studyResultId;
        this.userId = userId;
        this.eventType = eventType;
        this.targetId = targetId;
        this.correct = correct;
        this.score = score;
        this.createdAt = createdAt;
    }
}