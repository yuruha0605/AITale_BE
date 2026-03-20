package com.aitale.analytics.infrastructure;

import com.aitale.analytics.domain.UserGrowthSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserGrowthSnapshotRepository extends JpaRepository<UserGrowthSnapshot, Long> {

    Optional<UserGrowthSnapshot> findByUserIdAndSnapshotDate(Long userId, LocalDate snapshotDate);

    Optional<UserGrowthSnapshot> findTopByUserIdOrderBySnapshotDateDesc(Long userId);

    Optional<UserGrowthSnapshot> findTopByUserIdAndSnapshotDateLessThanOrderBySnapshotDateDesc(
        Long userId,
        LocalDate snapshotDate
    );

    List<UserGrowthSnapshot> findByUserIdAndSnapshotDateGreaterThanOrderBySnapshotDateAsc(
        Long userId,
        LocalDate snapshotDate
    );
}