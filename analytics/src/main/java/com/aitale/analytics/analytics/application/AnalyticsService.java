package com.aitale.analytics.analytics.application;

import com.aitale.analytics.analytics.domain.EventLog;
import com.aitale.analytics.analytics.dto.request.EventCreateRequest;
import com.aitale.analytics.analytics.dto.response.AccuracyResponse;
import com.aitale.analytics.analytics.dto.response.EventResponse;
import com.aitale.analytics.analytics.infrastructure.EventLogRepository;
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
        return eventLogRepository.findByUserId(userId).stream()
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
        List<EventLog> events = eventLogRepository.findByUserId(userId);

        long totalCount = events.size();
        long correctCount = events.stream()
            .filter(event -> Boolean.TRUE.equals(event.getCorrect()))
            .count();

        double accuracy = totalCount == 0 ? 0.0 : (double) correctCount / totalCount;

        return new AccuracyResponse(userId, totalCount, correctCount, accuracy);
    }
}