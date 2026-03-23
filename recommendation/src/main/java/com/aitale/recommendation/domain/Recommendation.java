package com.aitale.recommendation.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "recommendation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long storyId;

    private Integer rankOrder;

    private String title;

    private String reason;

    private Integer basedAge;

    private Integer basedLevel;

    private String basedDifficulty;

    private LocalDateTime generatedAt;

    private LocalDateTime expiresAt;

    @Builder
    public Recommendation(
        Long userId,
        Long storyId,
        Integer rankOrder,
        String title,
        String reason,
        Integer basedAge,
        Integer basedLevel,
        String basedDifficulty,
        LocalDateTime generatedAt,
        LocalDateTime expiresAt
    ) {
        this.userId = userId;
        this.storyId = storyId;
        this.rankOrder = rankOrder;
        this.title = title;
        this.reason = reason;
        this.basedAge = basedAge;
        this.basedLevel = basedLevel;
        this.basedDifficulty = basedDifficulty;
        this.generatedAt = generatedAt;
        this.expiresAt = expiresAt;
    }
}