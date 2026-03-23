package com.aitale.story.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record StoryImageGenerateRequest(
    @NotBlank(message = "이미지 스타일은 필수입니다.")
    String style
) {

}