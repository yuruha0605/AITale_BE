package com.example.gamification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor // 테스트를 위해 생성자를 추가해두는 것이 좋습니다.
public class RewardRequest {
    private Long userId;
    private String scoreGrade; // 상, 중, 하
    private String sourceType; // QUIZ, DIAGNOSIS 등

    // --- 민균 님이 추가해야 할 핵심 필드 (이게 없어서 에러가 났던 거예요!) ---
    private int correctCount; // 맞힌 정답 개수 (0~5)
    private boolean isBonusSolved; // 난이도 업 문제 해결 여부
}