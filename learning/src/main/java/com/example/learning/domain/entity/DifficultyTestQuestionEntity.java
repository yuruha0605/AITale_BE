package com.example.learning.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Entity
@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "difficulty_test_question")
public class DifficultyTestQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_question_id")
    private Long id;

    @Column(name = "target_age_group")
    private Integer targetAgeGroup;

    private String question;

    @Column(columnDefinition = "TEXT")
    private String options;

    @Column(name = "correct_answer")
    private Integer correctAnswer;

    @Column(name = "page_number")
    private Integer pageNumber;


}
