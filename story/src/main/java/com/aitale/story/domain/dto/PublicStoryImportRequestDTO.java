package com.aitale.story.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublicStoryImportRequestDTO {

    private Long genreId;
    private String keyword;
    private Integer pageNo;
    private Integer numOfRows;
}
