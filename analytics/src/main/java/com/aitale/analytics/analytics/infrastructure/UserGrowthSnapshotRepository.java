package com.aitale.analytics.analytics.infrastructure;

import com.aitale.analytics.analytics.domain.UserGrowthSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserGrowthSnapshotRepository extends JpaRepository<UserGrowthSnapshot, Long> {

    Optional<UserGrowthSnapshot> findTopByUserIdOrderBySnapshotDateDesc(Long userId);
}