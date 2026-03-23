package com.aitale.ai.presentation;

import com.aitale.ai.application.OpenAiRecommendationService;
import com.aitale.ai.dto.AiRecommendationRequest;
import com.aitale.ai.dto.AiRecommendationResult;
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