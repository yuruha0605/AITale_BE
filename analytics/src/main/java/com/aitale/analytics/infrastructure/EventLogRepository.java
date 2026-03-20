package com.aitale.analytics.infrastructure;

import com.aitale.analytics.domain.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    List<EventLog> findByUserIdOrderByCreatedAtDesc(Long userId);

    boolean existsByStudyResultId(String studyResultId);
}