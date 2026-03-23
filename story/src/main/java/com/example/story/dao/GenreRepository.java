package com.example.story.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.story.domain.entity.GenreEntity;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {
}
