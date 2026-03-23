package com.example.story.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.example.story.dao.GenreRepository;
import com.example.story.dao.StoryRepository;
import com.example.story.domain.dto.GenreCreateRequestDTO;
import com.example.story.domain.dto.GenreResponseDTO;
import com.example.story.domain.dto.PublicStoryImportRequestDTO;
import com.example.story.domain.dto.PublicStoryImportResultDTO;
import com.example.story.domain.dto.PublicStoryItemDTO;
import com.example.story.domain.dto.StoryCreateRequestDTO;
import com.example.story.domain.dto.StoryResponseDTO;
import com.example.story.domain.entity.GenreEntity;
import com.example.story.domain.entity.StoryEntity;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class StoryService {

    private final GenreRepository genreRepository;
    private final StoryRepository storyRepository;
    private final PublicStoryApiClient publicStoryApiClient;

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

    public PublicStoryImportResultDTO importStoriesFromPublicApi(PublicStoryImportRequestDTO request) {
        validateGenreExists(request.getGenreId());

        int pageNo = request.getPageNo() == null ? 1 : request.getPageNo();
        int numOfRows = request.getNumOfRows() == null ? 20 : request.getNumOfRows();

        List<PublicStoryItemDTO> items = publicStoryApiClient.fetchStories(request.getKeyword(), pageNo, numOfRows);

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

    private int getCharCount(String content) {
        if (!StringUtils.hasText(content)) {
            return 0;
        }
        return content.length();
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
