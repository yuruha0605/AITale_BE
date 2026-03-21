package com.example.learning.domain.dto;

import com.example.learning.domain.entity.Difficulty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SubmitResponseDTO {

    private int baseCorrectCount;
    private int bonusCorrectCount;
    private int totalScore;
    private boolean hasBonus; // 기본문제 다 맞춰서 보너스 문제 가능 여부
    private Difficulty nextDifficulty; // 보너스 문제 난이도
}
