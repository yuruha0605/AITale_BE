package com.aitale.story.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aitale.story.domain.entity.GenreEntity;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
}
