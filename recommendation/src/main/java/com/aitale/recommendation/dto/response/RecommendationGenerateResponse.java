package com.aitale.recommendation.dto.response;

import java.time.LocalDateTime;

public record RecommendationGenerateResponse(Long userId, Integer recommendedCount,
                                             LocalDateTime generatedAt, String status
) {

}