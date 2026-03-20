package com.aitale.analytics.analytics.dto.request;

import com.aitale.analytics.analytics.domain.EventType;

import java.time.LocalDateTime;

public record EventCreateRequest(Long userId, EventType eventType, Long targetId, Boolean correct,
                                 Integer score, LocalDateTime createdAt) {

}
