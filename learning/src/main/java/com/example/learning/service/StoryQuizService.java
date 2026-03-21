package com.example.learning.service;

import com.example.learning.dao.StoryQuizQuestionRepository;
import com.example.learning.dao.StudyRecordRepository;
import com.example.learning.domain.dto.AnswerDTO;
import com.example.learning.domain.dto.QuestionDTO;
import com.example.learning.domain.dto.SubmitResponseDTO;
import com.example.learning.domain.dto.WrongAnswerExplanationDTO;
import com.example.learning.domain.entity.Difficulty;
import com.example.learning.domain.entity.StoryQuizQuestionEntity;
import com.example.learning.domain.entity.StudyRecordEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoryQuizService {

        private final StoryQuizQuestionRepository quizRepository;
        private final StudyRecordRepository recordRepository;
        private final AiExplanationService aiExplanationService;
        private final ObjectMapper objectMapper;

        private static final int BONUS_COUNT = 2;

        // 퀴즈 조회
        public List<QuestionDTO> getQuiz(Long storyId, Difficulty difficulty) {
                List<StoryQuizQuestionEntity> questions = quizRepository.findByStoryIdAndDifficultyOrderByIdAsc(storyId,
                                difficulty);

                return questions.stream()
                                .map(q -> QuestionDTO.builder().id(q.getId()).question(q.getQuestion())
                                                .options(parseOptions(q.getOptions())).build())
                                .collect(Collectors.toList());
        }

        // 기본 문제 제출
        public SubmitResponseDTO submitBaseQuiz(Long userId, Long storyId, List<AnswerDTO> answers) {
                int baseScore = 0;
                int correctCount = 0;

                List<Long> ids = answers.stream()
                                .map(AnswerDTO::getQuestionId)
                                .toList();
                List<StoryQuizQuestionEntity> baseQuestions = quizRepository.findAllById(ids);

                Map<Long, StoryQuizQuestionEntity> questionMap = baseQuestions.stream()
                                .collect(Collectors.toMap(
                                                StoryQuizQuestionEntity::getId,
                                                q -> q));

                List<WrongAnswerExplanationDTO> explanations = new ArrayList<>();

                for (AnswerDTO answer : answers) {

                        StoryQuizQuestionEntity q = questionMap.get(answer.getQuestionId());
                        if (q == null)
                                continue;

                        if (q.getCorrectAnswer().equals(answer.getAnswer())) {

                                correctCount++;
                                baseScore += q.getDifficulty().getScore();

                        } else {

                                String explanation = aiExplanationService.generateExplanation(
                                                q.getQuestion(),
                                                q.getOptions(),
                                                answer.getAnswer(),
                                                q.getCorrectAnswer());

                                explanations.add(
                                                WrongAnswerExplanationDTO.builder()
                                                                .questionId(q.getId())
                                                                .explanation(explanation)
                                                                .build());
                        }
                }

                Difficulty baseDifficulty = baseQuestions.isEmpty()
                                ? Difficulty.LOW
                                : baseQuestions.get(0).getDifficulty();

                boolean hasBonus = baseDifficulty != Difficulty.HIGH
                                && correctCount == baseQuestions.size();

                Difficulty nextDifficulty = null;

                if (hasBonus) {
                        nextDifficulty = baseDifficulty == Difficulty.LOW
                                        ? Difficulty.MEDIUM
                                        : Difficulty.HIGH;
                }

                StudyRecordEntity record = StudyRecordEntity.builder()
                                .userId(userId)
                                .storyId(storyId)
                                .baseCorrectCount(correctCount)
                                .bonusCorrectCount(0)
                                .isBonusTriggered(false)
                                .isHighestDifficulty(baseDifficulty == Difficulty.HIGH)
                                .totalScore(baseScore)
                                .createdAt(LocalDateTime.now())
                                .build();

                recordRepository.save(record);

                return SubmitResponseDTO.builder()
                                .baseCorrectCount(correctCount)
                                .bonusCorrectCount(0)
                                .totalScore(baseScore)
                                .hasBonus(hasBonus)
                                .nextDifficulty(nextDifficulty)
                                .explanations(explanations)
                                .build();
        }

        // 보너스 문제 조회
        public List<QuestionDTO> getBonusQuestions(Long storyId, Difficulty nextDifficulty) {
                if (nextDifficulty == null)
                        return Collections.emptyList();

                return quizRepository.findByStoryIdAndDifficultyOrderByIdAsc(storyId, nextDifficulty)
                                .stream()
                                .limit(BONUS_COUNT)
                                .map(q -> QuestionDTO.builder()
                                                .id(q.getId())
                                                .question(q.getQuestion())
                                                .options(parseOptions(q.getOptions()))
                                                .build())
                                .toList();
        }

        // 보너스 문제 제출 (기존 기본 문제 기록 업데이트)
        public SubmitResponseDTO submitBonusQuiz(Long userId, Long storyId, Difficulty bonusDifficulty,
                        List<AnswerDTO> answers) {

                // 기존 기본 문제 기록 가져오기
                StudyRecordEntity record = recordRepository
                                .findTopByUserIdAndStoryIdOrderByCreatedAtDesc(userId, storyId)
                                .orElseThrow(() -> new RuntimeException("기본문제 기록이 없습니다."));

                int bonusScore = 0;
                int bonusCorrectCount = 0;

                List<Long> ids = answers.stream()
                                .map(AnswerDTO::getQuestionId)
                                .toList();

                List<StoryQuizQuestionEntity> bonusQuestions = quizRepository.findAllById(ids);

                Map<Long, StoryQuizQuestionEntity> questionMap = bonusQuestions.stream()
                                .collect(Collectors.toMap(
                                                StoryQuizQuestionEntity::getId,
                                                q -> q));

                List<WrongAnswerExplanationDTO> explanations = new ArrayList<>();

                for (AnswerDTO answer : answers) {

                        StoryQuizQuestionEntity q = questionMap.get(answer.getQuestionId());
                        if (q == null)
                                continue;

                        if (q.getCorrectAnswer().equals(answer.getAnswer())) {

                                bonusCorrectCount++;
                                bonusScore += q.getDifficulty().getScore();

                        } else {

                                String explanation = aiExplanationService.generateExplanation(
                                                q.getQuestion(),
                                                q.getOptions(),
                                                answer.getAnswer(),
                                                q.getCorrectAnswer());

                                explanations.add(
                                                WrongAnswerExplanationDTO.builder()
                                                                .questionId(q.getId())
                                                                .explanation(explanation)
                                                                .build());
                        }
                }

                record.setBonusCorrectCount(bonusCorrectCount);
                record.setTotalScore(record.getTotalScore() + bonusScore);
                record.setBonusTriggered(true);

                recordRepository.save(record);

                return SubmitResponseDTO.builder()
                                .baseCorrectCount(record.getBaseCorrectCount())
                                .bonusCorrectCount(bonusCorrectCount)
                                .totalScore(record.getTotalScore())
                                .hasBonus(false)
                                .nextDifficulty(null)
                                .explanations(explanations)
                                .build();
        }

        private List<String> parseOptions(String optionsJson) {

                try {
                        return objectMapper.readValue(
                                        optionsJson,
                                        new TypeReference<List<String>>() {
                                        });
                } catch (Exception e) {
                        return Collections.emptyList();
                }
        }
}