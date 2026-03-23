package com.aitale.aiservice.dto;

import java.util.List;

public record AiRecommendationRequest(Integer age, Integer level, String difficulty,
                                      List<String> interests, Integer size,
                                      List<AiStoryCandidate> stories
) {

}