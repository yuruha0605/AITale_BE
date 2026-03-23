package com.aitale.story.domain.dto.response;

public record StoryGenerateResponse(Long storyId, String title, String content, Integer charCount,
                                    String genre, Integer targetAge, String difficulty) {

}