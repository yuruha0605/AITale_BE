package com.aitale.recommendation.dto.response;

import java.util.List;

public record UserProfileResponse(Long userId, Integer age, Integer currentLevel,
                                  String assignedDifficulty, List<String> interests
) {

}