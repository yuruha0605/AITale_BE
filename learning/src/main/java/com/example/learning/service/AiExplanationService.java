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
                                        초등학생이 이해할 수 있도록 문제의 정답을 설명해주세요.

                                        문제: %s
                                        사용자의 답: %s
                                        정답: %s
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