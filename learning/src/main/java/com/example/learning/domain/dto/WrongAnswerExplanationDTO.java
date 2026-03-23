package com.example.learning.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WrongAnswerExplanationDTO {

    private Long questionId;
    private String explanation;

}
