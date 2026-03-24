package com.aitale.user.domain.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AssignDifficultyRequest(
    @NotBlank
    String difficulty
) {

}