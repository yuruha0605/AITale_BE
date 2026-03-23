package com.aitale.recommendation.infrastructure;

import com.aitale.recommendation.dto.response.StoryCandidatesResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "story-service")
public interface StoryServiceClient {

    @GetMapping("/internal/stories")
    StoryCandidatesResponse getStoryCandidates(
        @RequestParam(required = false) String difficulty,
        @RequestParam(required = false) Integer size
    );
}