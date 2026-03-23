package com.aitale.recommendation.dto.request;

import java.util.List;

public record InternalRecommendationBuildRequest(Long userId, Integer age, Integer level,
                                                 String difficulty, List<String> interests,
                                                 List<Long> excludeStoryIds) {

}
