package com.aitale.recommendation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record RecommendationListResponse(Long userId, LocalDateTime generatedAt,
                                         LocalDateTime expiresAt,
                                         List<RecommendationItemResponse> recommendations
) {

}
