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
                                        %s

                                        [학생이 고른 답]
                                        %s

                                        [정답]
                                        %s

                                        요구사항:
                                        - 인사말은 쓰지 않는다.
                                        - 번호나 목록 형식(1., 2., ** 등)을 사용하지 않는다.
                                        - 하나의 자연스러운 해설 문단으로 작성한다.
                                        - 학생이 고른 답이 왜 틀렸는지와 정답이 되는 이유를 함께 설명한다.
                                        - 초등학생이 이해할 수 있도록 쉽고 친절한 말로 3~4문장 정도로 작성한다.
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