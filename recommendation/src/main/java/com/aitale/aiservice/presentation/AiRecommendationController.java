package com.aitale.aiservice.presentation;

import com.aitale.aiservice.application.OpenAiRecommendationService;
import com.aitale.aiservice.dto.AiRecommendationRequest;
import com.aitale.aiservice.dto.AiRecommendationResult;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal")
public class AiRecommendationController {

    private final OpenAiRecommendationService openAiRecommendationService;

    @PostMapping("/recommendations")
    public AiRecommendationResult recommend(
        @Valid @RequestBody AiRecommendationRequest request
    ) {
        return openAiRecommendationService.recommend(request);
    }
}