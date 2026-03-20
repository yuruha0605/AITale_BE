package com.example.learning.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.example.learning.dao.DifficultyTestQuestionRepository;
import com.example.learning.domain.dto.QuestionDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DifficultyTestService {

    private final DifficultyTestQuestionRepository questionRepository;
    private final ObjectMapper objectMapper;

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


}
