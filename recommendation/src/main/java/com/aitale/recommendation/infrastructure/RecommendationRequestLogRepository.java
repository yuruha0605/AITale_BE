package com.aitale.recommendation.infrastructure;

import com.aitale.recommendation.domain.RecommendationRequestLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRequestLogRepository
    extends JpaRepository<RecommendationRequestLog, Long> {

    Page<RecommendationRequestLog> findByUserIdOrderByGeneratedAtDesc(
        Long userId,
        Pageable pageable
    );
}