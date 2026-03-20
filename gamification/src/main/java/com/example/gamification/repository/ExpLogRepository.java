package com.example.gamification.repository;

import com.example.gamification.domain.ExpLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpLogRepository extends JpaRepository<ExpLog, Long> {
}
