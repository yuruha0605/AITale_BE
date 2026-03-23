package com.aitale.story.dao;

import com.aitale.story.domain.entity.GenreEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<GenreEntity, Long> {

    Optional<GenreEntity> findByGenreName(String genreName);
}