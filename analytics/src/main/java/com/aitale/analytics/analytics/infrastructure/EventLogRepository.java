package com.aitale.analytics.analytics.infrastructure;

import com.aitale.analytics.analytics.domain.EventLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventLogRepository extends JpaRepository<EventLog, Long> {

    List<EventLog> findByUserId(Long userId);
}
