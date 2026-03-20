package com.aitale.analytics.analytics.dto.response;

import com.aitale.analytics.analytics.domain.EventType;

import java.time.LocalDateTime;

public record EventResponse(Long id, Long userId, EventType eventType, Long targetId,
                            Boolean correct, Integer score, LocalDateTime createdAt) {

}
