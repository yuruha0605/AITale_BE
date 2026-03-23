package com.aitale.recommendation.infrastructure;

import com.aitale.recommendation.domain.Recommendation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

    List<Recommendation> findByUserIdOrderByRankOrderAsc(Long userId);

    void deleteByUserId(Long userId);
}