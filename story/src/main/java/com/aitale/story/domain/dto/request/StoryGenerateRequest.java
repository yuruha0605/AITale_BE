package com.aitale.story.domain.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StoryGenerateRequest(
    @NotBlank(message = "제목은 필수입니다.")
    String title,

    @NotBlank(message = "주제는 필수입니다.")
    String topic,

    @NotBlank(message = "장르는 필수입니다.")
    String genre,

    @NotNull(message = "대상 연령은 필수입니다.")
    Integer targetAge,

    @NotBlank(message = "난이도는 필수입니다.")
    String difficulty,

    @NotNull(message = "목표 글자 수는 필수입니다.")
    Integer targetLength
) {
}