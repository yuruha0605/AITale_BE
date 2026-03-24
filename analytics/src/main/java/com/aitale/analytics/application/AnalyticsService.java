package com.aitale.analytics.application;

import com.aitale.analytics.domain.DailyScoreStat;
import com.aitale.analytics.domain.EventLog;
import com.aitale.analytics.domain.UserGrowthSnapshot;
import com.aitale.analytics.dto.request.EventCreateRequest;
import com.aitale.analytics.dto.response.AccuracyResponse;
import com.aitale.analytics.dto.response.DailyScoreItemResponse;
import com.aitale.analytics.dto.response.DailyScoreResponse;
import com.aitale.analytics.dto.response.EventResponse;
import com.aitale.analytics.exception.AnalyticsErrorCode;
import com.aitale.analytics.exception.AnalyticsException;
import com.aitale.analytics.infrastructure.DailyScoreStatRepository;
import com.aitale.analytics.infrastructure.EventLogRepository;
import com.aitale.analytics.infrastructure.UserGrowthSnapshotRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

    private final EventLogRepository eventLogRepository;
    private final UserGrowthSnapshotRepository userGrowthSnapshotRepository;
    private final DailyScoreStatRepository dailyScoreStatRepository;

    @Transactional
    public EventResponse saveEvent(EventCreateRequest request) {
        EventLog eventLog =
            EventLog.builder()
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
            saved.getCreatedAt());
    }

    public List<EventResponse> getUserEvents(Long userId) {
        return eventLogRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
            .map(
                event ->
                    new EventResponse(
                        event.getId(),
                        event.getUserId(),
                        event.getEventType(),
                        event.getTargetId(),
                        event.getCorrect(),
                        event.getScore(),
                        event.getCreatedAt()))
            .toList();
    }

    public AccuracyResponse getAccuracy(Long userId) {
        UserGrowthSnapshot snapshot =
            userGrowthSnapshotRepository
                .findTopByUserIdOrderBySnapshotDateDesc(userId)
                .orElseThrow(
                    () -> new AnalyticsException(AnalyticsErrorCode.GROWTH_SNAPSHOT_NOT_FOUND));

        return new AccuracyResponse(
            userId,
            snapshot.getTotalQuizCount(),
            snapshot.getTotalCorrectCount(),
            snapshot.getOverallCorrectRate());
    }

    public DailyScoreResponse getDailyScores(Long userId, LocalDate from, LocalDate to) {
        List<DailyScoreItemResponse> dailyScores =
            dailyScoreStatRepository.findByUserIdAndStatDateBetweenOrderByStatDateAsc(userId, from,
                    to)
                .stream()
                .map(this::toDailyScoreItemResponse)
                .toList();

        return new DailyScoreResponse(userId, from, to, dailyScores);
    }

    private DailyScoreItemResponse toDailyScoreItemResponse(DailyScoreStat stat) {
        return new DailyScoreItemResponse(
            stat.getStatDate(),
            stat.getTotalScoreSum(),
            stat.getQuizCount(),
            stat.getCorrectCount(),
            stat.getWrongCount(),
            stat.getAvgScore());
    }
}