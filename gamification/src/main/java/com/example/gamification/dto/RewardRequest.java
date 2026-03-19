// 소연님(Learning Service)이 퀴즈 결과를 보낼 때 담아줄 데이터 규격입니다.
package com.example.gamification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RewardRequest {
    private Long userId;
    private String scoreGrade; // 상, 중, 하
    private String sourceType; // QUIZ, DIAGNOSIS 등
}