package com.aitale.story.ctrl;

import com.aitale.story.domain.dto.response.StoryCandidatesResponse;
import com.aitale.story.service.StoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/stories")
public class InternalStoryController {

    private final StoryService storyService;

    @GetMapping
    public StoryCandidatesResponse getStoryCandidates(
        @RequestParam(required = false) String difficulty,
        @RequestParam(defaultValue = "5") Integer size
    ) {
        return storyService.getStoryCandidates(difficulty, size);
    }
}