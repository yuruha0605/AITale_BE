package com.example.gamification.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QuizResultEvent {
    private Long userId; // 유저 ID
    private int totalScore; // 소연님 DTO의 totalScore
    private int correctCount; // baseCorrectCount + bonusCorrectCount
    private boolean isBonusSolved; // bonusCorrectCount > 0 이면 true
}