package com.aitale.story.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "story_tbl")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "story_id")
    private Long storyId;

    @Column(name = "genre_id", nullable = false)
    private Long genreId;

    @Column(name = "api_id")
    private Long apiId;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "author", length = 255)
    private String author;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "char_count")
    private Integer charCount;

    @Lob
    @Column(name = "ai_image_url", columnDefinition = "LONGTEXT")
    private String aiImageUrl;

    @Column(name = "source_url", length = 1000)
    private String sourceUrl;

    @Column(name = "origin_thumb_url", length = 1000)
    private String originThumbUrl;

    public void updateAiImageUrl(String aiImageUrl) {
        this.aiImageUrl = aiImageUrl;
    }
}