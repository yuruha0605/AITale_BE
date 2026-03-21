package com.example.learning.dao;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.learning.domain.entity.StudyRecordEntity;

@Repository
public interface StudyRecordRepository extends JpaRepository<StudyRecordEntity, Long> {

    // 최신 기록 1개 가져오기
    Optional<StudyRecordEntity> findTopByUserIdAndStoryIdOrderByCreatedAtDesc(Long userId, Long storyId);

}
