package com.example.learning.ctrl;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.learning.domain.dto.AnswerDTO;
import com.example.learning.domain.dto.QuestionDTO;
import com.example.learning.service.DifficultyTestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/difficulty_test")
@RequiredArgsConstructor
public class DifficultyTestController {

    private final DifficultyTestService service;

    @GetMapping
    public List<QuestionDTO> getQuestions(@RequestParam("ageGroup") Integer ageGroup,
            @RequestParam("page") Integer page) {

        return service.getQuestions(ageGroup, page);
    }

    @PostMapping("/submit")
    public String submitTest(@RequestBody AnswerDTO request) {
        return service.submitTest(request.getUserId(), request.getAnswers()).name();
    }

}
