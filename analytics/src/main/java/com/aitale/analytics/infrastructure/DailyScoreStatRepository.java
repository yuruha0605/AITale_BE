package com.aitale.analytics.infrastructure;

import com.aitale.analytics.domain.DailyScoreStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyScoreStatRepository extends JpaRepository<DailyScoreStat, Long> {

    List<DailyScoreStat> findByUserIdOrderByStatDateAsc(Long userId);

    Optional<DailyScoreStat> findByUserIdAndStatDate(Long userId, LocalDate statDate);
}