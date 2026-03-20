package com.example.learning.dao;

import com.example.learning.domain.entity.DifficultyTestQuestionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DifficultyTestQuestionRepository extends JpaRepository<DifficultyTestQuestionEntity, Long>{
    
    List<DifficultyTestQuestionEntity> findByTargetAgeGroupAndPageNumberOrderById(
            
            Integer targetAgeGroup,
            Integer pageNumber
    );

}
