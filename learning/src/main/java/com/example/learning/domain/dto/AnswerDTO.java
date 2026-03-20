package com.example.learning.domain.dto;

import java.util.ArrayList;
import java.util.List;

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
public class AnswerDTO {

    private Long userId;
    private Long questionId;
    private Integer answer;

    @Builder.Default
    private List<AnswerDTO> answers = new ArrayList<>();

}
