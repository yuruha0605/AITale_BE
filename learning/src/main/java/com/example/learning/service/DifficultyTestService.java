package com.example.learning.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.learning.dao.DifficultyTestQuestionRepository;
import com.example.learning.domain.dto.AnswerDTO;
import com.example.learning.domain.dto.QuestionDTO;
import com.example.learning.domain.entity.Difficulty;
import com.example.learning.domain.entity.DifficultyTestQuestionEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DifficultyTestService {

    private final DifficultyTestQuestionRepository questionRepository;
    private final ObjectMapper objectMapper;
    private final UserOpenFeignService userOpenFeignService;

    // 문제 조회
    public List<QuestionDTO> getQuestions(Integer ageGroup, Integer page) {
        return questionRepository.findByTargetAgeGroupAndPageNumberOrderById(ageGroup, page)
                .stream().map(q -> {
                    List<String> options;
                    try {
                        options = objectMapper.readValue(q.getOptions(),
                                new TypeReference<List<String>>() {});
                    } catch (Exception e) {
                        throw new RuntimeException("options parsing error", e);
                    }
                    return QuestionDTO.builder().id(q.getId()).question(q.getQuestion())
                            .options(options).build();
                }).toList();
    }

    // 제출
    public Difficulty submitTest(Long userId, List<AnswerDTO> answers) {
        List<Long> ids = answers.stream().map(AnswerDTO::getQuestionId).toList();

        List<DifficultyTestQuestionEntity> questions = questionRepository.findAllById(ids);

        Map<Long, DifficultyTestQuestionEntity> map = questions.stream()
                .collect(Collectors.toMap(DifficultyTestQuestionEntity::getId, q -> q));

        int correctCount = 0;
        for (AnswerDTO answer : answers) {
            DifficultyTestQuestionEntity q = map.get(answer.getQuestionId());
            if (q != null && answer.getAnswer() != null
                    && answer.getAnswer().equals(q.getCorrectAnswer())) {
                correctCount++;
            }
        }

        // 난이도 판정
        Difficulty assignedDifficulty;
        if (correctCount <= 3) {
            assignedDifficulty = Difficulty.LOW;
        } else if (correctCount <= 7) {
            assignedDifficulty = Difficulty.MEDIUM;
        } else
            assignedDifficulty = Difficulty.HIGH;

        // // FeignClient로 user-service 호출
        // Map<String, String> body = Map.of("assignedDifficulty", assignedDifficulty.name());
        // userOpenFeignService.updateAssignedDifficulty(userId, body);

        return assignedDifficulty;
    }
}
