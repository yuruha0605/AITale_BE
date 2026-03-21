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
import com.example.learning.domain.dto.SubmitResponseDTO;
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

    // 기본 문제 답안 제출
    @PostMapping("/submit")
    public SubmitResponseDTO submitBaseQuiz(@RequestBody AnswerDTO request) {
        return storyQuizService.submitBaseQuiz(request.getUserId(), request.getStoryId(),
                request.getAnswers());
    }

    // 보너스 문제 조회
    @GetMapping("/bonus")
    public List<QuestionDTO> getBonusQuestions(@RequestParam("storyId") Long storyId,
            @RequestParam("nextDifficulty") Difficulty nextDifficulty) {
        return storyQuizService.getBonusQuestions(storyId, nextDifficulty);
    }

    // 보너스 문제 답안 제출
    @PostMapping("/bonus/submit")
    public SubmitResponseDTO submitBonusQuiz(@RequestBody AnswerDTO request) {
        return storyQuizService.submitBonusQuiz(request.getUserId(), request.getStoryId(),
                request.getBonusDifficulty(), request.getAnswers());
    }

}
