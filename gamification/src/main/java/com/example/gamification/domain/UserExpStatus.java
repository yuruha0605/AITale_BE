// 사용자 경험치 상태를 나타내는 엔티티 클래스입니다. 각 사용자는 고유한 userId를 가지며,
package com.example.gamification.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_exp_status")
public class UserExpStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long userId;

    private Integer totalExp = 0;
    private Integer currentLevel = 1;

    @Builder
    public UserExpStatus(Long userId) {
        this.userId = userId;
        this.totalExp = 0;
        this.currentLevel = 1;
    }

    public void addExp(int amount) {
        if (amount > 0) {
            this.totalExp += amount;
            updateLevel();
        }
    }

    private void updateLevel() {
        // 100점당 1레벨업 로직 (가독성을 위해 80자 이내 줄바꿈)
        int nextLevel = (this.totalExp / 100) + 1;
        if (nextLevel > this.currentLevel) {
            this.currentLevel = nextLevel;
        }
    }
}
