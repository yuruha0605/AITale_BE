package com.example.learning.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiExplanationService {

        private final RestTemplate restTemplate;

        @Value("${openai.api-key}")
        private String apiKey;

        @Value("${openai.url}")
        private String apiUrl;

        public String generateExplanation(
                        String question,
                        Integer userAnswer,
                        Integer correctAnswer) {

                try {

                        String prompt = """
                                        너는 초등학생에게 독해 문제를 설명해주는 친절한 선생님이다.

                                        다음 문제의 정답을 이해하기 쉽게 설명해줘.

                                        [문제]
                                        {question}

                                        [학생이 고른 답]
                                        {userAnswer}

                                        [정답]
                                        {correctAnswer}

                                        다음 형식으로 설명해줘.

                                        1. 왜 학생의 답이 틀렸는지 간단히 설명
                                        2. 문제에서 중요한 단서가 무엇인지 설명
                                        3. 정답이 되는 이유를 이야기하듯 설명

                                        설명은 초등학생이 이해할 수 있도록 쉽고 친절하게 작성해줘.
                                                """.formatted(question, userAnswer, correctAnswer);

                        Map<String, Object> body = new HashMap<>();

                        body.put("model", "gpt-4o-mini");

                        body.put("messages", List.of(
                                        Map.of(
                                                        "role", "user",
                                                        "content", prompt)));

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.setBearerAuth(apiKey);

                        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

                        Map response = restTemplate.postForObject(apiUrl, request, Map.class);

                        List choices = (List) response.get("choices");
                        Map choice = (Map) choices.get(0);
                        Map message = (Map) choice.get("message");

                        return message.get("content").toString();

                } catch (Exception e) {

                        return "해설을 불러오지 못했습니다.";

                }
        }
}