package com.aitale.story.service;

import com.aitale.story.domain.dto.ai.AiImageResult;
import com.aitale.story.domain.dto.ai.AiStoryResult;
import com.aitale.story.domain.dto.request.StoryGenerateRequest;
import com.aitale.story.domain.entity.StoryEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OpenAiStoryAiClient implements StoryAiClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${openai.base-url:https://api.openai.com}")
    private String baseUrl;

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.chat-model:gpt-4.1-mini}")
    private String chatModel;

    @Value("${openai.image-model:dall-e-3}")
    private String imageModel;

    @Override
    public AiStoryResult generateStory(StoryGenerateRequest request) {
        String prompt = buildStoryPrompt(request);

        HttpHeaders headers = createHeaders();
        Map<String, Object> body = Map.of(
            "model", chatModel,
            "messages", List.of(
                Map.of("role", "system", "content", "너는 아동용 한국어 동화를 작성하는 작가다."),
                Map.of("role", "user", "content", prompt)
            ),
            "temperature", 0.9
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/v1/chat/completions",
            HttpMethod.POST,
            entity,
            Map.class
        );

        String content = extractChatContent(response.getBody());

        try {
            return objectMapper.readValue(content, AiStoryResult.class);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("AI 동화 응답 파싱에 실패했습니다.", e);
        }
    }

    @Override
    public AiImageResult generateStoryImage(StoryEntity story, String style) {
        String prompt = buildImagePrompt(story, style);

        HttpHeaders headers = createHeaders();
        Map<String, Object> body = Map.of(
            "model", imageModel,
            "prompt", prompt,
            "size", "1024x1024",
            "response_format", "url"
        );

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
            baseUrl + "/v1/images/generations",
            HttpMethod.POST,
            entity,
            Map.class
        );

        String imageUrl = extractImageUrl(response.getBody());

        return new AiImageResult(imageUrl);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private String extractChatContent(Map responseBody) {
        if (responseBody == null) {
            throw new IllegalStateException("AI 응답이 비어 있습니다.");
        }

        Object rawChoices = responseBody.get("choices");
        if (!(rawChoices instanceof List<?> choices) || choices.isEmpty()) {
            throw new IllegalStateException("AI 응답 choices가 비어 있습니다.");
        }

        Object firstChoice = choices.get(0);
        if (!(firstChoice instanceof Map<?, ?> choiceMap)) {
            throw new IllegalStateException("AI 응답 형식이 올바르지 않습니다.");
        }

        Object rawMessage = choiceMap.get("message");
        if (!(rawMessage instanceof Map<?, ?> messageMap)) {
            throw new IllegalStateException("AI 응답 message 형식이 올바르지 않습니다.");
        }

        Object content = messageMap.get("content");
        if (content == null || !StringUtils.hasText(content.toString())) {
            throw new IllegalStateException("AI 응답 content가 비어 있습니다.");
        }

        return content.toString();
    }

    private String extractImageUrl(Map responseBody) {
        if (responseBody == null) {
            throw new IllegalStateException("AI 이미지 응답이 비어 있습니다.");
        }

        Object rawData = responseBody.get("data");
        if (!(rawData instanceof List<?> dataList) || dataList.isEmpty()) {
            throw new IllegalStateException("AI 이미지 응답 data가 비어 있습니다.");
        }

        Object first = dataList.get(0);
        if (!(first instanceof Map<?, ?> firstMap)) {
            throw new IllegalStateException("AI 이미지 응답 형식이 올바르지 않습니다.");
        }

        Object url = firstMap.get("url");
        if (url == null || !StringUtils.hasText(url.toString())) {
            throw new IllegalStateException("AI 이미지 URL이 비어 있습니다.");
        }

        return url.toString();
    }

    private String buildStoryPrompt(StoryGenerateRequest request) {
        return """
            아래 조건에 맞는 한국어 아동용 동화를 작성해 주세요.
            
            [조건]
            - 제목 힌트: %s
            - 주제: %s
            - 장르: %s
            - 대상 연령: %d세
            - 난이도: %s
            - 목표 글자 수: %d자 내외
            - 문장은 쉽고 부드럽게 작성
            - 유해하거나 폭력적인 표현 금지
            - 마지막에는 자연스러운 교훈 포함
            
            [중요]
            반드시 아래 JSON 형식으로만 응답하세요.
            다른 설명은 절대 붙이지 마세요.
            
            {
              "title": "동화 제목",
              "content": "동화 본문"
            }
            """.formatted(
            defaultText(request.title(), "따뜻한 동화"),
            defaultText(request.topic(), "우정"),
            defaultText(request.genre(), "창작동화"),
            defaultNumber(request.targetAge(), 8),
            defaultText(request.difficulty(), "중"),
            defaultNumber(request.targetLength(), 1000)
        );
    }

    private String buildImagePrompt(StoryEntity story, String style) {
        return """
            아동용 동화책 삽화 스타일의 이미지를 생성해 주세요.
            
            [제목]
            %s
            
            [본문]
            %s
            
            [스타일]
            %s
            
            [조건]
            - 따뜻하고 밝은 분위기
            - 아이들이 보기 좋은 동화책 삽화 느낌
            - 텍스트 삽입 금지
            - 잔인하거나 무서운 표현 금지
            """.formatted(
            defaultText(story.getTitle(), "동화 삽화"),
            defaultText(story.getContent(), "따뜻한 분위기의 동화 장면"),
            defaultText(style, "부드러운 수채화 스타일")
        );
    }

    private String defaultText(String value, String defaultValue) {
        return StringUtils.hasText(value) ? value : defaultValue;
    }

    private int defaultNumber(Integer value, int defaultValue) {
        return value != null ? value : defaultValue;
    }
}