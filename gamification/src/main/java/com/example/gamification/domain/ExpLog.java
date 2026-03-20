// 경험치가 언제, 왜 발생했는지 기록하는 로그 엔티티입니다.
package com.example.gamification.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Integer amount;
    private String sourceType; // 예: QUIZ, DIAGNOSIS
    private String description;
    private LocalDateTime earnedAt;

    @Builder
    public ExpLog(Long userId, Integer amount, String sourceType, String description) {
        this.userId = userId;
        this.amount = amount;
        this.sourceType = sourceType;
        this.description = description;
        this.earnedAt = LocalDateTime.now();
    }
}
