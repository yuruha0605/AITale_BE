package com.example.gamification.repository;

import com.example.gamification.domain.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {
    boolean existsByUserIdAndBadgeId(Long userId, Long badgeId);
}
