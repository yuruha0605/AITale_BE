package com.aitale.recommendation.dto.response;

import java.time.LocalDateTime;

public record RecommendationLogResponse(Long id, String interestSummary, Integer level,
                                        String difficulty, Integer recommendedCount,
                                        LocalDateTime generatedAt, String status) {

}