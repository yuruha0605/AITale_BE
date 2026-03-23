package com.example.story.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.story.domain.entity.StoryEntity;

public interface StoryRepository extends JpaRepository<StoryEntity, Long> {

    List<StoryEntity> findByGenreIdOrderByStoryIdAsc(Long genreId);

    Optional<StoryEntity> findByApiId(Long apiId);
}
