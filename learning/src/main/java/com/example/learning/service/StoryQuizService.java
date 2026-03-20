package com.example.learning.service;

import com.example.learning.dao.StoryQuizQuestionRepository;
import com.example.learning.domain.dto.QuestionDTO;
import com.example.learning.domain.entity.Difficulty;
import com.example.learning.domain.entity.StoryQuizQuestionEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryQuizService {

    private final StoryQuizQuestionRepository quizRepository;

    private final ObjectMapper objectMapper;

    // 퀴즈 조회
    public List<QuestionDTO> getQuiz(Long storyId, Difficulty difficulty) {
        List<StoryQuizQuestionEntity> questions =
                quizRepository.findByStoryIdAndDifficultyOrderByIdAsc(storyId, difficulty);

        return questions.stream()
                .map(q -> QuestionDTO.builder().id(q.getId()).question(q.getQuestion())
                        .options(parseOptions(q.getOptions())).build())
                .collect(Collectors.toList());
    }

    // 선택지 JSON 문자열 -> List<String>
    private List<String> parseOptions(String optionsJson) {
        try {
            return objectMapper.readValue(optionsJson, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

}
