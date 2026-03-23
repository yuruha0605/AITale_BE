package com.example.story.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryResponseDTO {

    private Long storyId;
    private Long genreId;
    private Long apiId;
    private String title;
    private String author;
    private String content;
    private Integer charCount;
    private String aiImageUrl;
    private String sourceUrl;
    private String originThumbUrl;
}
