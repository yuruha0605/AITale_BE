package com.aitale.analytics.analytics.application;

import com.aitale.analytics.analytics.domain.EventLog;
import com.aitale.analytics.analytics.domain.UserGrowthSnapshot;
import com.aitale.analytics.analytics.dto.request.EventCreateRequest;
import com.aitale.analytics.analytics.dto.response.AccuracyResponse;
import com.aitale.analytics.analytics.dto.response.EventResponse;
import com.aitale.analytics.analytics.infrastructure.EventLogRepository;
import com.aitale.analytics.analytics.infrastructure.UserGrowthSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final EventLogRepository eventLogRepository;
    private final UserGrowthSnapshotRepository userGrowthSnapshotRepository;

    @Transactional
    public EventResponse saveEvent(EventCreateRequest request) {
        EventLog eventLog = EventLog.builder().userId(request.userId())
            .eventType(request.eventType()).targetId(request.targetId())
            .correct(request.correct()).score(request.score())
            .createdAt(request.createdAt() != null ? request.createdAt() : LocalDateTime.now())
            .build();

        EventLog saved = eventLogRepository.save(eventLog);

        return new EventResponse(saved.getId(), saved.getUserId(), saved.getEventType(),
            saved.getTargetId(), saved.getCorrect(), saved.getScore(), saved.getCreatedAt());
    }

    public List<EventResponse> getUserEvents(Long userId) {
        return eventLogRepository.findByUserId(userId).stream()
            .map(event -> new EventResponse(event.getId(), event.getUserId(),
                event.getEventType(), event.getTargetId(), event.getCorrect(),
                event.getScore(), event.getCreatedAt()))
            .toList();
    }

    public AccuracyResponse getAccuracy(Long userId) {
        UserGrowthSnapshot snapshot = userGrowthSnapshotRepository
            .findTopByUserIdOrderBySnapshotDateDesc(userId)
            .orElseThrow(() -> new RuntimeException("성장 스냅샷이 없습니다."));

        return new AccuracyResponse(
            userId,
            snapshot.getTotalQuizCount(),
            snapshot.getTotalCorrectCount(),
            snapshot.getOverallCorrectRate()
        );
    }
}
