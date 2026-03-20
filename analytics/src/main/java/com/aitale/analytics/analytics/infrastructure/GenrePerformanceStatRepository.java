package com.aitale.analytics.analytics.infrastructure;

import com.aitale.analytics.analytics.domain.GenrePerformanceStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GenrePerformanceStatRepository extends JpaRepository<GenrePerformanceStat, Long> {

    List<GenrePerformanceStat> findByUserId(Long userId);

    Optional<GenrePerformanceStat> findByUserIdAndGenre(Long userId, String genre);
}