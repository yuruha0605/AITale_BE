package com.example.learning.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "story_quiz_question")
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StoryQuizQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quiz_question_id")
    private Long id;

    @Column(name = "story_id", nullable = false)
    private Long storyId;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false, length = 1000)
    private String options;

    @Column(name = "correct_answer", nullable = false)
    private Integer correctAnswer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Difficulty difficulty;

    @Column(nullable = false)
    private boolean isBonus;            // 보너스 문제 여부 (서비스에서 동적으로 처리)
    
}
