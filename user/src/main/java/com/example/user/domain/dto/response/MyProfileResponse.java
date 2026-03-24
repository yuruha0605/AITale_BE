package com.example.user.domain.dto.response;


public record MyProfileResponse(Long userId, String email, int age, int currentLevel,
                                String assignedDifficulty) {

}