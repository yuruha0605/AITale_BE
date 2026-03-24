package com.example.gamification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RewardRequest {
    private Long userId;
    private String scoreGrade;
    private String sourceType;
    private int correctCount;
    private boolean isBonusSolved;
}