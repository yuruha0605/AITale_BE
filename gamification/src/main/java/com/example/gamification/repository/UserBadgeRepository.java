package com.example.gamification.repository;

import com.example.gamification.domain.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    
    // 배지 중복 체크용
    boolean existsByUserIdAndBadgeId(Long userId, Long badgeId);

    // 사용자별 전체 배지 조회용
    List<UserBadge> findAllByUserId(Long userId);
}