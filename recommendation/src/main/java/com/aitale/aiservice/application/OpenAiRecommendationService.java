package com.aitale.aiservice.application;

import com.aitale.aiservice.dto.AiRecommendationRequest;
import com.aitale.aiservice.dto.AiRecommendationResult;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.StructuredChatCompletion;
import com.openai.models.chat.completions.StructuredChatCompletionCreateParams;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpenAiRecommendationService {

    private final OpenAIClient openAIClient;
    private final RecommendationPromptService promptService;

    public AiRecommendationResult recommend(@Valid AiRecommendationRequest request) {

        String prompt = promptService.createPrompt(request);

        StructuredChatCompletionCreateParams<AiRecommendationResult> params =
            ChatCompletionCreateParams.builder()
                .addUserMessage(prompt)
                .model(ChatModel.GPT_5_2)
                .responseFormat(AiRecommendationResult.class)
                .build();

        return openAIClient.chat()
            .completions()
            .create(params)
            .choices()
            .stream()
            .map(StructuredChatCompletion.Choice::message)
            .flatMap(message -> message.content().stream())
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("AI 추천 응답이 비어 있습니다."));
    }
}