package com.example.learning.ctrl;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.learning.domain.dto.QuestionDTO;
import com.example.learning.domain.entity.Difficulty;
import com.example.learning.service.StoryQuizService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/story_quiz")
@RequiredArgsConstructor
public class StoryQuizController {

    private final StoryQuizService storyQuizService;

    @GetMapping
    public List<QuestionDTO> getQuiz(@RequestParam("storyId") Long storyId,
            @RequestParam("difficulty") Difficulty difficulty) {
        return storyQuizService.getQuiz(storyId, difficulty);
    }

}
