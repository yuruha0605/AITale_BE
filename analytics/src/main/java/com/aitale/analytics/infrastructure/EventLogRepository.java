package com.aitale.analytics.infrastructure;

import com.aitale.analytics.domain.EventLog;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {

  List<EventLog> findByUserIdOrderByCreatedAtDesc(Long userId);

  boolean existsByStudyResultId(String studyResultId);
}
