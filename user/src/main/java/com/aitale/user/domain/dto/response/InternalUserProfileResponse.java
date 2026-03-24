package com.aitale.user.domain.dto.response;

import java.util.List;

public record InternalUserProfileResponse(Long userId, String email, int age, int currentLevel,
                                          String assignedDifficulty, List<String> interests) {

}