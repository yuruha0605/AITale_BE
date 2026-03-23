package com.aitale.story.service;

import com.aitale.story.dao.GenreRepository;
import com.aitale.story.dao.StoryRepository;
import com.aitale.story.domain.dto.GenreCreateRequestDTO;
import com.aitale.story.domain.dto.GenreResponseDTO;
import com.aitale.story.domain.dto.PublicStoryImportRequestDTO;
import com.aitale.story.domain.dto.PublicStoryImportResultDTO;
import com.aitale.story.domain.dto.PublicStoryItemDTO;
import com.aitale.story.domain.dto.StoryCreateRequestDTO;
import com.aitale.story.domain.dto.StoryResponseDTO;
import com.aitale.story.domain.dto.ai.AiImageResult;
import com.aitale.story.domain.dto.ai.AiStoryResult;
import com.aitale.story.domain.dto.request.StoryGenerateRequest;
import com.aitale.story.domain.dto.request.StoryImageGenerateRequest;
import com.aitale.story.domain.dto.response.StoryGenerateResponse;
import com.aitale.story.domain.dto.response.StoryImageGenerateResponse;
import com.aitale.story.domain.entity.GenreEntity;
import com.aitale.story.domain.entity.StoryEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class StoryService {

    private final GenreRepository genreRepository;
    private final StoryRepository storyRepository;
    private final PublicStoryApiClient publicStoryApiClient;
    private final StoryAiClient storyAiClient;

    public GenreResponseDTO createGenre(GenreCreateRequestDTO request) {
        GenreEntity saved = genreRepository.save(GenreEntity.builder()
            .genreName(request.getGenreName())
            .build());

        return GenreResponseDTO.builder()
            .genreId(saved.getGenreId())
            .genreName(saved.getGenreName())
            .build();
    }

    @Transactional(readOnly = true)
    public List<GenreResponseDTO> getGenres() {
        return genreRepository.findAll().stream()
            .map(genre -> GenreResponseDTO.builder()
                .genreId(genre.getGenreId())
                .genreName(genre.getGenreName())
                .build())
            .toList();
    }

    public StoryResponseDTO createStory(StoryCreateRequestDTO request) {
        validateGenreExists(request.getGenreId());

        StoryEntity saved = storyRepository.save(StoryEntity.builder()
            .genreId(request.getGenreId())
            .apiId(request.getApiId())
            .title(request.getTitle())
            .author(request.getAuthor())
            .content(request.getContent())
            .charCount(getCharCount(request.getContent()))
            .aiImageUrl(request.getAiImageUrl())
            .sourceUrl(request.getSourceUrl())
            .originThumbUrl(request.getOriginThumbUrl())
            .build());

        return toResponse(saved);
    }

    public StoryGenerateResponse generateStory(StoryGenerateRequest request) {
        GenreEntity genreEntity = findGenreByName(request.genre());
        Long genreId = genreEntity.getGenreId();

        AiStoryResult aiResult = storyAiClient.generateStory(request);

        if (!StringUtils.hasText(aiResult.content())) {
            throw new IllegalStateException("AI가 동화 내용을 생성하지 못했습니다.");
        }

        StoryEntity saved = storyRepository.save(
            StoryEntity.builder()
                .genreId(genreId)
                .apiId(null)
                .title(resolveTitle(request.title(), aiResult.title()))
                .author("AI")
                .content(aiResult.content())
                .charCount(getCharCount(aiResult.content()))
                .aiImageUrl(null)
                .sourceUrl(null)
                .originThumbUrl(null)
                .build()
        );

        return new StoryGenerateResponse(
            saved.getStoryId(),
            saved.getTitle(),
            saved.getContent(),
            saved.getCharCount(),
            request.genre(),
            request.targetAge(),
            request.difficulty()
        );
    }

    public StoryImageGenerateResponse generateStoryImage(Long storyId,
        StoryImageGenerateRequest request) {
        StoryEntity story = storyRepository.findById(storyId)
            .orElseThrow(() -> new IllegalArgumentException("스토리를 찾을 수 없습니다. storyId=" + storyId));

        AiImageResult aiImageResult = storyAiClient.generateStoryImage(story, request.style());
        story.updateAiImageUrl(aiImageResult.imageUrl());

        return new StoryImageGenerateResponse(
            story.getStoryId(),
            story.getTitle(),
            story.getAiImageUrl()
        );
    }

    @Transactional(readOnly = true)
    public StoryResponseDTO getStory(Long storyId) {
        StoryEntity story = storyRepository.findById(storyId)
            .orElseThrow(() -> new RuntimeException("스토리를 찾을 수 없습니다. id=" + storyId));
        return toResponse(story);
    }

    @Transactional(readOnly = true)
    public List<StoryResponseDTO> getStories(Long genreId) {
        List<StoryEntity> stories = genreId == null
            ? storyRepository.findAll()
            : storyRepository.findByGenreIdOrderByStoryIdAsc(genreId);

        return stories.stream().map(this::toResponse).toList();
    }

    public PublicStoryImportResultDTO importStoriesFromPublicApi(
        PublicStoryImportRequestDTO request) {
        validateGenreExists(request.getGenreId());

        int pageNo = request.getPageNo() == null ? 1 : request.getPageNo();
        int numOfRows = request.getNumOfRows() == null ? 20 : request.getNumOfRows();

        List<PublicStoryItemDTO> items = publicStoryApiClient.fetchStories(request.getKeyword(),
            pageNo, numOfRows);

        int savedCount = 0;
        for (PublicStoryItemDTO item : items) {
            StoryEntity story;
            if (item.getApiId() == null) {
                story = StoryEntity.builder().build();
            } else {
                story = storyRepository.findByApiId(item.getApiId())
                    .orElse(StoryEntity.builder().apiId(item.getApiId()).build());
            }

            StoryEntity updated = StoryEntity.builder()
                .storyId(story.getStoryId())
                .genreId(request.getGenreId())
                .apiId(item.getApiId())
                .title(item.getTitle())
                .author(item.getAuthor())
                .content(item.getContent())
                .charCount(getCharCount(item.getContent()))
                .aiImageUrl(story.getAiImageUrl())
                .sourceUrl(item.getSourceUrl())
                .originThumbUrl(item.getOriginThumbUrl())
                .build();

            storyRepository.save(updated);
            savedCount++;
        }

        return PublicStoryImportResultDTO.builder()
            .requestedCount(items.size())
            .savedCount(savedCount)
            .build();
    }

    private void validateGenreExists(Long genreId) {
        if (genreId == null || !genreRepository.existsById(genreId)) {
            throw new RuntimeException("유효하지 않은 장르입니다. genreId=" + genreId);
        }
    }

    private GenreEntity findGenreByName(String genreName) {
        if (!StringUtils.hasText(genreName)) {
            throw new IllegalArgumentException("장르는 필수입니다.");
        }

        return genreRepository.findByGenreName(genreName)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장르입니다. genre=" + genreName));
    }

    private int getCharCount(String content) {
        if (!StringUtils.hasText(content)) {
            return 0;
        }
        return content.length();
    }

    private String resolveTitle(String requestTitle, String aiTitle) {
        if (StringUtils.hasText(requestTitle)) {
            return requestTitle;
        }
        if (StringUtils.hasText(aiTitle)) {
            return aiTitle;
        }
        return "AI 생성 동화";
    }

    private StoryResponseDTO toResponse(StoryEntity story) {
        return StoryResponseDTO.builder()
            .storyId(story.getStoryId())
            .genreId(story.getGenreId())
            .apiId(story.getApiId())
            .title(story.getTitle())
            .author(story.getAuthor())
            .content(story.getContent())
            .charCount(story.getCharCount())
            .aiImageUrl(story.getAiImageUrl())
            .sourceUrl(story.getSourceUrl())
            .originThumbUrl(story.getOriginThumbUrl())
            .build();
    }
}