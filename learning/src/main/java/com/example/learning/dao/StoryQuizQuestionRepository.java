package com.example.learning.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.learning.domain.entity.Difficulty;
import com.example.learning.domain.entity.StoryQuizQuestionEntity;

public interface StoryQuizQuestionRepository
        extends JpaRepository<StoryQuizQuestionEntity, Long> {

    List<StoryQuizQuestionEntity> findByStoryIdAndDifficultyOrderByIdAsc(
            Long storyId, Difficulty difficulty);

    List<StoryQuizQuestionEntity> findByStoryIdOrderByIdAsc(Long storyId);
    

}
