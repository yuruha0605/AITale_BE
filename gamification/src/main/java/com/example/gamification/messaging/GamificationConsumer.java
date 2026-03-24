package com.example.gamification.messaging;

import com.example.gamification.dto.QuizResultEvent;
import com.example.gamification.dto.RewardRequest;
import com.example.gamification.service.GamificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class GamificationConsumer {

    private final GamificationService gamificationService;

    @KafkaListener(topics = "quiz-result-topic", groupId = "gamification-group")
    public void consumeQuizResult(QuizResultEvent event) {
        log.info("Learning 서비스로부터 수신된 결과: {}", event);

        // 1. 점수 ---> 등급 변환 로직 (예시: 80점 이상은 상, 50점 이상은 중, 그 외는 하)
        String grade = "하";
        if (event.getTotalScore() >= 80)
            grade = "상";
        else if (event.getTotalScore() >= 50)
            grade = "중";

        // 2. 기존에 만들었던 보상 처리 로직 호출
        RewardRequest request = new RewardRequest();
        request.setUserId(event.getUserId());
        request.setScoreGrade(grade);
        request.setCorrectCount(event.getCorrectCount());
        request.setBonusSolved(event.isBonusSolved());
        request.setSourceType("QUIZ");

        gamificationService.processReward(request);
        log.info("사용자 {}번에 대한 자동 보상 처리 완료!", event.getUserId());
    }
}