package com.aitale.recommendation.application;

import com.aitale.recommendation.domain.Recommendation;
import com.aitale.recommendation.domain.RecommendationRequestLog;
import com.aitale.recommendation.domain.RecommendationStatus;
import com.aitale.recommendation.dto.ai.AiRecommendationResult;
import com.aitale.recommendation.dto.ai.AiRecommendedStory;
import com.aitale.recommendation.dto.request.InternalRecommendationBuildRequest;
import com.aitale.recommendation.dto.response.RecommendationGenerateResponse;
import com.aitale.recommendation.dto.response.StoryCandidateResponse;
import com.aitale.recommendation.dto.response.UserProfileResponse;
import com.aitale.recommendation.exception.RecommendationErrorCode;
import com.aitale.recommendation.exception.RecommendationException;
import com.aitale.recommendation.infrastructure.RecommendationRepository;
import com.aitale.recommendation.infrastructure.RecommendationRequestLogRepository;
import com.aitale.recommendation.infrastructure.StoryServiceClient;
import com.aitale.recommendation.infrastructure.UserServiceClient;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationCommandService {

    private static final int DEFAULT_SIZE = 5;
    private static final int MAX_SIZE = 20;

    private final RecommendationRepository recommendationRepository;
    private final RecommendationRequestLogRepository recommendationRequestLogRepository;
    private final RecommendationCacheService recommendationCacheService;
    private final UserServiceClient userServiceClient;
    private final StoryServiceClient storyServiceClient;
    private final RecommendationAiService recommendationAiService;

    public RecommendationGenerateResponse generateRecommendations(Long userId, Integer size) {
        int recommendSize = (size == null) ? DEFAULT_SIZE : size;
        validateSize(recommendSize);

        UserProfileResponse userProfile = userServiceClient.getUserProfile(userId);
        if (userProfile == null) {
            throw new RecommendationException(RecommendationErrorCode.USER_PROFILE_NOT_FOUND);
        }

        return buildAndSave(userProfile, recommendSize, Collections.emptyList());
    }

    public RecommendationGenerateResponse buildInternal(
        InternalRecommendationBuildRequest request) {
        UserProfileResponse userProfile = new UserProfileResponse(
            request.userId(),
            request.age(),
            request.level(),
            request.difficulty(),
            request.interests()
        );

        List<Long> excludeStoryIds =
            request.excludeStoryIds() == null ? List.of() : request.excludeStoryIds();

        return buildAndSave(userProfile, 5, excludeStoryIds);
    }

    public void deleteRecommendationCache(Long userId) {
//        recommendationCacheService.evict(userId);
//        recommendationRepository.deleteByUserId(userId);
        // TODO: 해결 시 삭제
        try {
            recommendationCacheService.evict(userId);
        } catch (Exception e) {
            // ignore
        }
        recommendationRepository.deleteByUserId(userId);
    }

    private RecommendationGenerateResponse buildAndSave(
        UserProfileResponse userProfile,
        int size,
        List<Long> excludeStoryIds
    ) {
//        StoryCandidatesResponse candidateResponse =
//            storyServiceClient.getStoryCandidates(userProfile.assignedDifficulty(), size * 3);
//
//        if (candidateResponse == null
//            || candidateResponse.stories() == null
//            || candidateResponse.stories().isEmpty()) {
//            saveFailedLog(userProfile);
//            throw new RecommendationException(RecommendationErrorCode.STORY_CANDIDATE_NOT_FOUND);
//        }
//
//        List<StoryCandidateResponse> filteredStories = candidateResponse.stories().stream()
//            .filter(story -> !excludeStoryIds.contains(story.storyId()))
//            .toList();
//
//        if (filteredStories.isEmpty()) {
//            saveFailedLog(userProfile);
//            throw new RecommendationException(RecommendationErrorCode.STORY_CANDIDATE_NOT_FOUND);
//        }

        // TODO: 테스트용 코드
        List<StoryCandidateResponse> filteredStories = List.of(
                new StoryCandidateResponse(201L, "토끼와 해님", "동물", 1200),
                new StoryCandidateResponse(202L, "숲속 친구들의 약속", "자연", 1500),
                new StoryCandidateResponse(203L, "곰의 모험", "모험", 1300)
            ).stream()
            .filter(story -> !excludeStoryIds.contains(story.storyId()))
            .toList();

        if (filteredStories.isEmpty()) {
            saveFailedLog(userProfile);
            throw new RecommendationException(RecommendationErrorCode.STORY_CANDIDATE_NOT_FOUND);
        }
        // 여기까지

        log.info("추천 생성 시작 - userId={}, size={}, difficulty={}, interests={}",
            userProfile.userId(),
            size,
            userProfile.assignedDifficulty(),
            userProfile.interests());

        log.info("추천 후보 동화 수={}, storyIds={}",
            filteredStories.size(),
            filteredStories.stream().map(StoryCandidateResponse::storyId).toList());

        AiRecommendationResult aiResult;
        try {
            aiResult = recommendationAiService.recommend(userProfile, filteredStories, size);
            log.info("AI 추천 응답 수={}",
                aiResult == null || aiResult.recommendations() == null
                    ? null
                    : aiResult.recommendations().size());
        } catch (Exception e) {
            log.error("AI 추천 호출 실패 - userId={}", userProfile.userId(), e);
            saveFailedLog(userProfile);
            throw new RecommendationException(RecommendationErrorCode.RECOMMENDATION_BUILD_FAILED);
        }

        if (aiResult == null) {
            log.error("AI 추천 응답이 null 입니다. userId={}", userProfile.userId());
            saveFailedLog(userProfile);
            throw new RecommendationException(RecommendationErrorCode.RECOMMENDATION_BUILD_FAILED);
        }

        if (aiResult.recommendations() == null || aiResult.recommendations().isEmpty()) {
            log.error("AI 추천 목록이 비어 있습니다. userId={}, aiResult={}",
                userProfile.userId(), aiResult);
            saveFailedLog(userProfile);
            throw new RecommendationException(RecommendationErrorCode.RECOMMENDATION_BUILD_FAILED);
        }

        Map<Long, StoryCandidateResponse> storyMap = filteredStories.stream()
            .collect(Collectors.toMap(StoryCandidateResponse::storyId, Function.identity()));

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusHours(6);

        recommendationRepository.deleteByUserId(userProfile.userId());

        List<Recommendation> recommendations = aiResult.recommendations().stream()
            .filter(item -> item.storyId() != null)
            .filter(item -> storyMap.containsKey(item.storyId()))
            .limit(size)
            .map(item -> toRecommendation(item, storyMap.get(item.storyId()), userProfile, now,
                expiresAt))
            .toList();

        if (recommendations.isEmpty()) {
            saveFailedLog(userProfile);
            throw new RecommendationException(RecommendationErrorCode.RECOMMENDATION_BUILD_FAILED);
        }

        recommendationRepository.saveAll(recommendations);
        recommendationCacheService.put(userProfile.userId(), recommendations);

        recommendationRequestLogRepository.save(
            RecommendationRequestLog.builder()
                .userId(userProfile.userId())
                .interestSummary(joinInterests(userProfile.interests()))
                .level(userProfile.currentLevel())
                .difficulty(userProfile.assignedDifficulty())
                .recommendedCount(recommendations.size())
                .generatedAt(now)
                .status(RecommendationStatus.COMPLETED)
                .build()
        );

        return new RecommendationGenerateResponse(
            userProfile.userId(),
            recommendations.size(),
            now,
            RecommendationStatus.COMPLETED.name()
        );
    }

    private Recommendation toRecommendation(
        AiRecommendedStory item,
        StoryCandidateResponse story,
        UserProfileResponse userProfile,
        LocalDateTime now,
        LocalDateTime expiresAt
    ) {
        return Recommendation.builder()
            .userId(userProfile.userId())
            .storyId(story.storyId())
            .rankOrder(item.rankOrder())
            .title(story.title())
            .reason(item.reason())
            .basedAge(userProfile.age())
            .basedLevel(userProfile.currentLevel())
            .basedDifficulty(userProfile.assignedDifficulty())
            .generatedAt(now)
            .expiresAt(expiresAt)
            .build();
    }

    private void saveFailedLog(UserProfileResponse userProfile) {
        recommendationRequestLogRepository.save(
            RecommendationRequestLog.builder()
                .userId(userProfile.userId())
                .interestSummary(joinInterests(userProfile.interests()))
                .level(userProfile.currentLevel())
                .difficulty(userProfile.assignedDifficulty())
                .recommendedCount(0)
                .generatedAt(LocalDateTime.now())
                .status(RecommendationStatus.FAILED)
                .build()
        );
    }

    private void validateSize(int size) {
        if (size < 1 || size > MAX_SIZE) {
            throw new RecommendationException(RecommendationErrorCode.INVALID_RECOMMENDATION_SIZE);
        }
    }

    private String joinInterests(List<String> interests) {
        if (interests == null || interests.isEmpty()) {
            return "";
        }
        return String.join(",", interests);
    }

    // TODO: story 구현 완료 및 테스트 성공 시 삭제
    private List<StoryCandidateResponse> getMockStories() {
        return List.of(
            new StoryCandidateResponse(201L, "토끼와 해님", "동물", 1200),
            new StoryCandidateResponse(202L, "숲속 친구들의 약속", "자연", 1500),
            new StoryCandidateResponse(203L, "곰의 모험", "모험", 1300),
            new StoryCandidateResponse(204L, "바다를 건넌 새", "자연", 1100),
            new StoryCandidateResponse(205L, "용감한 다람쥐", "모험", 1400)
        );
    }
}