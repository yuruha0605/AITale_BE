package com.aitale.ai.dto;

import java.util.List;

public record AiRecommendationRequest(Integer age, Integer level, String difficulty,
                                      List<String> interests, Integer size,
                                      List<AiStoryCandidate> stories
) {

}