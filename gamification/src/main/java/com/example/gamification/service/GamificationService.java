// 경험치를 계산하고, 로그를 남기고, 레벨업을 처리하는 실질적인 로직이 들어갑니다.
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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class GamificationService {

    private final UserExpStatusRepository statusRepository;
    private final ExpLogRepository expLogRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    @Transactional
    public void processReward(RewardRequest request) {
        // 1. 사용자 경험치 상태 업데이트
        UserExpStatus status = statusRepository.findByUserId(request.getUserId())
                .orElseGet(() -> statusRepository.save(new UserExpStatus(request.getUserId())));

        int amount = calculateExp(request.getScoreGrade());
        status.addExp(amount);

        // 2. 경험치 로그 기록
        saveExpLog(request, amount);

        // 3. 특정 조건 만족 시 배지 지급 (예: 성적이 "상"인 경우)
        if ("상".equals(request.getScoreGrade())) {
            checkAndGiveBadge(request.getUserId(), "FIRST_PERFECT");
        }
    }

    private void saveExpLog(RewardRequest request, int amount) {
        ExpLog log = ExpLog.builder().userId(request.getUserId()).amount(amount)
                .sourceType(request.getSourceType()).description(request.getSourceType() + " 보상 획득")
                .build();
        expLogRepository.save(log);
    }

    private void checkAndGiveBadge(Long userId, String badgeType) {
        badgeRepository.findByType(badgeType).ifPresent(badge -> {
            boolean alreadyHas =
                    userBadgeRepository.existsByUserIdAndBadgeId(userId, badge.getId());

            if (!alreadyHas) {
                userBadgeRepository.save(new UserBadge(userId, badge.getId()));
            }
        });
    }

    private int calculateExp(String grade) {
        if ("상".equals(grade)) {
            return 100;
        }
        if ("중".equals(grade)) {
            return 50;
        }
        return 10;
    }
<<<<<<< HEAD

    // 사용자 상태 조회 로직
    public UserExpStatus getUserStatus(Long userId) {
        return statusRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자의 경험치 정보가 없습니다."));
    }

    // 사용자 배지 목록 조회 로직
    public List<UserBadge> getUserBadges(Long userId) {
        return userBadgeRepository.findAllByUserId(userId);
    }
}

=======
}
>>>>>>> 79eecd29d67268b1cfb7d8ae681127c961eaf057
