package com.aitale.analytics.analytics.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "event_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private Long targetId;

    private Boolean correct;

    private Integer score;

    private LocalDateTime createdAt;

    @Builder
    public EventLog(Long userId, EventType eventType, Long targetId, Boolean correct, Integer score,
            LocalDateTime createdAt) {
        this.userId = userId;
        this.eventType = eventType;
        this.targetId = targetId;
        this.correct = correct;
        this.score = score;
        this.createdAt = createdAt;
    }
}
