package com.aitale.analytics.application;

import com.aitale.analytics.domain.EventLog;
import com.aitale.analytics.domain.UserGrowthSnapshot;
import com.aitale.analytics.dto.request.EventCreateRequest;
import com.aitale.analytics.dto.response.AccuracyResponse;
import com.aitale.analytics.dto.response.EventResponse;
import com.aitale.analytics.exception.AnalyticsErrorCode;
import com.aitale.analytics.exception.AnalyticsException;
import com.aitale.analytics.infrastructure.EventLogRepository;
import com.aitale.analytics.infrastructure.UserGrowthSnapshotRepository;
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
        EventLog eventLog = EventLog.builder()
            .userId(request.userId())
            .eventType(request.eventType())
            .targetId(request.targetId())
            .correct(request.correct())
            .score(request.score())
            .createdAt(request.createdAt() != null ? request.createdAt() : LocalDateTime.now())
            .build();

        EventLog saved = eventLogRepository.save(eventLog);

        return new EventResponse(
            saved.getId(),
            saved.getUserId(),
            saved.getEventType(),
            saved.getTargetId(),
            saved.getCorrect(),
            saved.getScore(),
            saved.getCreatedAt()
        );
    }

    public List<EventResponse> getUserEvents(Long userId) {
        return eventLogRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(event -> new EventResponse(
                event.getId(),
                event.getUserId(),
                event.getEventType(),
                event.getTargetId(),
                event.getCorrect(),
                event.getScore(),
                event.getCreatedAt()
            ))
            .toList();
    }

    public AccuracyResponse getAccuracy(Long userId) {
        UserGrowthSnapshot snapshot = userGrowthSnapshotRepository
            .findTopByUserIdOrderBySnapshotDateDesc(userId)
            .orElseThrow(
                () -> new AnalyticsException(AnalyticsErrorCode.GROWTH_SNAPSHOT_NOT_FOUND));

        return new AccuracyResponse(
            userId,
            snapshot.getTotalQuizCount(),
            snapshot.getTotalCorrectCount(),
            snapshot.getOverallCorrectRate()
        );
    }
}