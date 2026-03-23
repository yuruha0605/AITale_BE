package com.example.gamification.service;

import com.example.gamification.domain.Badge;
import com.example.gamification.domain.ExpLog;
import com.example.gamification.domain.UserBadge;
import com.example.gamification.domain.UserExpStatus;
import com.example.gamification.dto.RewardRequest;
import com.example.gamification.repository.BadgeRepository;
import com.example.gamification.repository.ExpLogRepository;
import com.example.gamification.repository.UserBadgeRepository;
import com.example.gamification.repository.UserExpStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GamificationService {

    private final UserExpStatusRepository statusRepository;
    private final ExpLogRepository expLogRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    /**
     * 보상 처리 메인 로직: 경험치 업데이트, 로그 기록, 배지 지급을 통합 관리합니다.
     */
    @Transactional
    public void processReward(RewardRequest request) {
        // 1. 사용자 경험치 상태 업데이트 (해당 유저가 처음이면 새로 생성)
        UserExpStatus status = statusRepository.findByUserId(request.getUserId())
                .orElseGet(() -> statusRepository.save(new UserExpStatus(request.getUserId())));

        // 등급(상/중/하)에 따른 경험치 계산 및 유저 상태에 반영
        int amount = calculateExp(request.getScoreGrade());
        status.addExp(amount);

        // 2. 경험치 획득 로그 기록
        saveExpLog(request, amount);

        // 3. [노션 기획안 반영] 정답 개수 및 보너스 문제 여부에 따른 배지 지급 로직 실행
        assignBadgesByQuizResult(request);
    }

    /**
     * 노션 기획안에 따른 배지 지급 조건 검사 메서드
     */
    private void assignBadgesByQuizResult(RewardRequest request) {
        Long userId = request.getUserId();
        int correct = request.getCorrectCount();
        boolean bonus = request.isBonusSolved();

        // [조건 1] 5문제 모두 정답 + 난이도 업(보너스) 문제 해결 -> 플래티넘 책벌레
        if (correct == 5 && bonus) {
            checkAndGiveBadge(userId, "PLATINUM_BOOKWORM");
        }
        // [조건 2] 5문제 모두 정답 -> 금색 책벌레
        else if (correct == 5) {
            checkAndGiveBadge(userId, "GOLD_BOOKWORM");
        }
        // [조건 3] 3~4개 정답 -> 은색 책벌레
        else if (correct >= 3) {
            checkAndGiveBadge(userId, "SILVER_BOOKWORM");
        }
        // [조건 4] 2개 이하 정답 (최소 1개 이상 정답 시) -> 동화 완독 배지
        else if (correct >= 1) {
            checkAndGiveBadge(userId, "STORY_COMPLETE");
        }
    }

    /**
     * 경험치 로그 저장 (DB의 exp_log 테이블)
     */
    private void saveExpLog(RewardRequest request, int amount) {
        ExpLog log = ExpLog.builder()
                .userId(request.getUserId())
                .amount(amount)
                .sourceType(request.getSourceType())
                .description(request.getSourceType() + " 보상 획득 (" + request.getScoreGrade() + ")")
                .build();
        expLogRepository.save(log);
    }

    /**
     * 배지 지급 처리 (DB의 badge 테이블에서 타입을 찾아 user_badge에 저장, 중복 지급 방지)
     */
    private void checkAndGiveBadge(Long userId, String badgeType) {
        badgeRepository.findByType(badgeType).ifPresent(badge -> {
            // 해당 유저가 이미 해당 배지를 가지고 있는지 확인
            boolean alreadyHas = userBadgeRepository.existsByUserIdAndBadgeId(userId, badge.getId());

            if (!alreadyHas) {
                userBadgeRepository.save(new UserBadge(userId, badge.getId()));
            }
        });
    }

    /**
     * 성적 등급에 따른 경험치 산출
     */
    private int calculateExp(String grade) {
        if ("상".equals(grade))
            return 100;
        if ("중".equals(grade))
            return 50;
        return 10; // "하" 또는 기타 등급
    }

    /**
     * 사용자 경험치 상태 조회
     */
    public UserExpStatus getUserStatus(Long userId) {
        return statusRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자의 경험치 정보가 없습니다."));
    }

    /**
     * 사용자가 보유한 배지 목록 조회
     */
    public List<UserBadge> getUserBadges(Long userId) {
        return userBadgeRepository.findAllByUserId(userId);
    }
}