package com.aitale.analytics.analytics.application;

import com.aitale.analytics.analytics.domain.DailyScoreStat;
import com.aitale.analytics.analytics.domain.EventLog;
import com.aitale.analytics.analytics.domain.EventType;
import com.aitale.analytics.analytics.domain.GenrePerformanceStat;
import com.aitale.analytics.analytics.domain.UserGrowthSnapshot;
import com.aitale.analytics.analytics.dto.request.StudyResultCreateRequest;
import com.aitale.analytics.analytics.infrastructure.DailyScoreStatRepository;
import com.aitale.analytics.analytics.infrastructure.EventLogRepository;
import com.aitale.analytics.analytics.infrastructure.GenrePerformanceStatRepository;
import com.aitale.analytics.analytics.infrastructure.UserGrowthSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AnalyticsCommandService {

    private final DailyScoreStatRepository dailyScoreStatRepository;
    private final UserGrowthSnapshotRepository userGrowthSnapshotRepository;
    private final GenrePerformanceStatRepository genrePerformanceStatRepository;
    private final EventLogRepository eventLogRepository;

    public void saveStudyResult(StudyResultCreateRequest request) {
        saveEventLog(request);
        updateDailyScoreStat(request);
        updateUserGrowthSnapshot(request);
        updateGenrePerformanceStat(request);
    }

    private void saveEventLog(StudyResultCreateRequest request) {
        EventLog eventLog = EventLog.builder()
            .userId(request.userId())
            .eventType(EventType.QUIZ_SOLVED)
            .targetId(request.storyId())
            .correct(request.wrongCount() == 0)
            .score(request.totalScore())
            .createdAt(request.solvedAt())
            .build();

        eventLogRepository.save(eventLog);
    }

    private void updateDailyScoreStat(StudyResultCreateRequest request) {
        LocalDate statDate = request.solvedAt().toLocalDate();
        LocalDateTime now = LocalDateTime.now();

        dailyScoreStatRepository.findByUserIdAndStatDate(request.userId(), statDate)
            .ifPresentOrElse(
                stat -> {
                    int newTotalScoreSum = stat.getTotalScoreSum() + request.totalScore();
                    int newQuizCount = stat.getQuizCount() + request.quizCount();
                    int newCorrectCount = stat.getCorrectCount() + request.correctCount();
                    int newWrongCount = stat.getWrongCount() + request.wrongCount();
                    double newAvgScore =
                        newQuizCount == 0 ? 0.0 : (double) newTotalScoreSum / newQuizCount;

                    stat.updateStats(
                        newTotalScoreSum,
                        newQuizCount,
                        newCorrectCount,
                        newWrongCount,
                        newAvgScore,
                        now
                    );
                },
                () -> {
                    double avgScore = request.quizCount() == 0
                        ? 0.0
                        : (double) request.totalScore() / request.quizCount();

                    DailyScoreStat stat = DailyScoreStat.builder()
                        .userId(request.userId())
                        .statDate(statDate)
                        .totalScoreSum(request.totalScore())
                        .quizCount(request.quizCount())
                        .correctCount(request.correctCount())
                        .wrongCount(request.wrongCount())
                        .avgScore(avgScore)
                        .createdAt(now)
                        .updatedAt(now)
                        .build();

                    dailyScoreStatRepository.save(stat);
                }
            );
    }

    private void updateUserGrowthSnapshot(StudyResultCreateRequest request) {
        LocalDate snapshotDate = request.solvedAt().toLocalDate();
        LocalDateTime now = LocalDateTime.now();

        userGrowthSnapshotRepository.findTopByUserIdOrderBySnapshotDateDesc(request.userId())
            .ifPresentOrElse(
                snapshot -> {
                    int newTotalScore = snapshot.getTotalScore() + request.totalScore();
                    int newTotalQuizCount = snapshot.getTotalQuizCount() + request.quizCount();
                    int newTotalCorrectCount =
                        snapshot.getTotalCorrectCount() + request.correctCount();
                    double newOverallCorrectRate = newTotalQuizCount == 0
                        ? 0.0
                        : (double) newTotalCorrectCount / newTotalQuizCount;

                    snapshot.updateSnapshot(
                        request.currentLevel(),
                        newTotalScore,
                        newTotalQuizCount,
                        newTotalCorrectCount,
                        newOverallCorrectRate
                    );
                },
                () -> {
                    double overallCorrectRate = request.quizCount() == 0
                        ? 0.0
                        : (double) request.correctCount() / request.quizCount();

                    UserGrowthSnapshot snapshot = UserGrowthSnapshot.builder()
                        .userId(request.userId())
                        .snapshotDate(snapshotDate)
                        .currentLevel(request.currentLevel())
                        .totalScore(request.totalScore())
                        .totalQuizCount(request.quizCount())
                        .totalCorrectCount(request.correctCount())
                        .overallCorrectRate(overallCorrectRate)
                        .createdAt(now)
                        .build();

                    userGrowthSnapshotRepository.save(snapshot);
                }
            );
    }

    private void updateGenrePerformanceStat(StudyResultCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        genrePerformanceStatRepository.findByUserIdAndGenre(request.userId(), request.genre())
            .ifPresentOrElse(
                stat -> {
                    int oldAttemptCount = stat.getAttemptCount();

                    double oldTotalCorrect = stat.getCorrectRate() * oldAttemptCount;
                    double oldTotalScore = stat.getAvgScore() * oldAttemptCount;

                    int newAttemptCount = oldAttemptCount + request.quizCount();
                    double newTotalCorrect = oldTotalCorrect + request.correctCount();
                    double newTotalScore = oldTotalScore + request.totalScore();

                    double newCorrectRate =
                        newAttemptCount == 0 ? 0.0 : newTotalCorrect / newAttemptCount;
                    double newAvgScore =
                        newAttemptCount == 0 ? 0.0 : newTotalScore / newAttemptCount;

                    stat.updateStats(
                        newAttemptCount,
                        newCorrectRate,
                        newAvgScore,
                        request.solvedAt(),
                        now
                    );
                },
                () -> {
                    double correctRate = request.quizCount() == 0
                        ? 0.0
                        : (double) request.correctCount() / request.quizCount();
                    double avgScore = request.quizCount() == 0
                        ? 0.0
                        : (double) request.totalScore() / request.quizCount();

                    GenrePerformanceStat stat = GenrePerformanceStat.builder()
                        .userId(request.userId())
                        .genre(request.genre())
                        .attemptCount(request.quizCount())
                        .correctRate(correctRate)
                        .avgScore(avgScore)
                        .lastSolvedAt(request.solvedAt())
                        .createdAt(now)
                        .updatedAt(now)
                        .build();

                    genrePerformanceStatRepository.save(stat);
                }
            );
    }
}