// "어떤 유저(userId)가 어떤 배지(badgeId)를 언제(earnedAt) 얻었나"를 기록하는 연결 고리입니다.
package com.example.gamification.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_badge")
public class UserBadge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long badgeId; // Badge 엔티티의 PK 타입과 맞춥니다.

    private LocalDateTime earnedAt;

    @Builder
    public UserBadge(Long userId, Long badgeId) {
        this.userId = userId;
        this.badgeId = badgeId;
        this.earnedAt = LocalDateTime.now();
    }
}
