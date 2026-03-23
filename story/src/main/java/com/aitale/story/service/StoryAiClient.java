package com.aitale.story.service;

import com.aitale.story.domain.dto.ai.AiImageResult;
import com.aitale.story.domain.dto.ai.AiStoryResult;
import com.aitale.story.domain.dto.request.StoryGenerateRequest;
import com.aitale.story.domain.entity.StoryEntity;

public interface StoryAiClient {

    AiStoryResult generateStory(StoryGenerateRequest request);

    AiImageResult generateStoryImage(StoryEntity story, String style);
}