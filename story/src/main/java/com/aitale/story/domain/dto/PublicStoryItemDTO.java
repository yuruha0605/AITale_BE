package com.aitale.story.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicStoryItemDTO {

    private Long apiId;
    private String title;
    private String author;
    private String content;
    private String sourceUrl;
    private String originThumbUrl;
}
