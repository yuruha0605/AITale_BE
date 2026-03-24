package com.aitale.user.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "story_selection_tbl")
@Getter
@NoArgsConstructor
public class StorySelectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storySelectId;

    @Column(name = "user_system_id")
    private Long userSystemId;

    @Column(name = "story_id")
    private Long storyId;

}
