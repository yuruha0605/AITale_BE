package com.example.story.ctrl;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.story.domain.dto.GenreCreateRequestDTO;
import com.example.story.domain.dto.GenreResponseDTO;
import com.example.story.domain.dto.PublicStoryImportRequestDTO;
import com.example.story.domain.dto.PublicStoryImportResultDTO;
import com.example.story.domain.dto.StoryCreateRequestDTO;
import com.example.story.domain.dto.StoryResponseDTO;
import com.example.story.service.StoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

// MSA 구조에서 Story 도메인만 담당하는 Story-Service API
@Tag(name = "Story-Service", description = "스토리/장르/공공데이터 연동 API")
@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @Operation(summary = "장르 생성", description = "새로운 장르를 생성합니다.")
    @PostMapping("/genres")
    public ResponseEntity<GenreResponseDTO> createGenre(@RequestBody GenreCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.createGenre(request));
    }

    @Operation(summary = "장르 목록 조회", description = "등록된 장르 목록을 조회합니다.")
    @GetMapping("/genres")
    public ResponseEntity<List<GenreResponseDTO>> getGenres() {
        return ResponseEntity.ok(storyService.getGenres());
    }

    @Operation(summary = "스토리 생성", description = "스토리를 수동으로 생성합니다.")
    @PostMapping
    public ResponseEntity<StoryResponseDTO> createStory(@RequestBody StoryCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(storyService.createStory(request));
    }

    @Operation(summary = "스토리 단건 조회", description = "storyId로 스토리를 조회합니다.")
    @GetMapping("/{storyId}")
    public ResponseEntity<StoryResponseDTO> getStory(@PathVariable Long storyId) {
        return ResponseEntity.ok(storyService.getStory(storyId));
    }

    @Operation(summary = "스토리 목록 조회", description = "genreId 선택 필터로 스토리 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<StoryResponseDTO>> getStories(@RequestParam(required = false) Long genreId) {
        return ResponseEntity.ok(storyService.getStories(genreId));
    }

    @Operation(summary = "공공데이터 가져오기", description = "문화공공데이터 API에서 스토리 목록을 가져와 저장합니다.")
    @PostMapping("/import/public-data")
    public ResponseEntity<PublicStoryImportResultDTO> importStories(@RequestBody PublicStoryImportRequestDTO request) {
        return ResponseEntity.ok(storyService.importStoriesFromPublicApi(request));
    }
}
