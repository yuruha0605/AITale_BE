package com.aitale.recommendation.dto.response;

public record RecommendationItemResponse(Long storyId, Integer rankOrder, String title,
                                         String reason, Integer basedAge, Integer basedLevel,
                                         String basedDifficulty) {

}
