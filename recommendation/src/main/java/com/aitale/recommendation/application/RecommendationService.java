package com.aitale.recommendation.application;

import com.aitale.recommendation.domain.Recommendation;
import com.aitale.recommendation.dto.response.RecommendationItemResponse;
import com.aitale.recommendation.dto.response.RecommendationListResponse;
import com.aitale.recommendation.dto.response.RecommendationLogResponse;
import com.aitale.recommendation.exception.RecommendationErrorCode;
import com.aitale.recommendation.exception.RecommendationException;
import com.aitale.recommendation.infrastructure.RecommendationRepository;
import com.aitale.recommendation.infrastructure.RecommendationRequestLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final RecommendationRequestLogRepository recommendationRequestLogRepository;
    private final RecommendationCacheService recommendationCacheService;
    private final RecommendationCommandService recommendationCommandService;

    public RecommendationListResponse getRecommendations(Long userId, Integer size,
        boolean refresh) {
        if (refresh) {
            recommendationCommandService.generateRecommendations(userId, size);
        }

        List<Recommendation> cached = recommendationCacheService.get(userId);
        if (!cached.isEmpty()) {
            return toListResponse(userId, cached);
        }

        List<Recommendation> recommendations = recommendationRepository.findByUserIdOrderByRankOrderAsc(
            userId);

        if (recommendations.isEmpty()) {
            recommendationCommandService.generateRecommendations(userId, size);
            recommendations = recommendationRepository.findByUserIdOrderByRankOrderAsc(userId);
        }

        if (recommendations.isEmpty()) {
            throw new RecommendationException(RecommendationErrorCode.RECOMMENDATION_NOT_FOUND);
        }

        return toListResponse(userId, recommendations);
    }

    public Page<RecommendationLogResponse> getRecommendationLogs(Long userId, Integer page,
        Integer size) {
        return recommendationRequestLogRepository
            .findByUserIdOrderByGeneratedAtDesc(userId, PageRequest.of(page, size))
            .map(log -> new RecommendationLogResponse(
                log.getId(),
                log.getInterestSummary(),
                log.getLevel(),
                log.getDifficulty(),
                log.getRecommendedCount(),
                log.getGeneratedAt(),
                log.getStatus().name()
            ));
    }

    private RecommendationListResponse toListResponse(Long userId,
        List<Recommendation> recommendations) {
        Recommendation first = recommendations.get(0);

        List<RecommendationItemResponse> items = recommendations.stream()
            .map(recommendation -> new RecommendationItemResponse(
                recommendation.getStoryId(),
                recommendation.getRankOrder(),
                recommendation.getTitle(),
                recommendation.getReason(),
                recommendation.getBasedAge(),
                recommendation.getBasedLevel(),
                recommendation.getBasedDifficulty()
            ))
            .toList();

        return new RecommendationListResponse(
            userId,
            first.getGeneratedAt(),
            first.getExpiresAt(),
            items
        );
    }
}