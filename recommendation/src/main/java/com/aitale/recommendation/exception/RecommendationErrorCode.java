package com.aitale.recommendation.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum RecommendationErrorCode {

    RECOMMENDATION_NOT_FOUND(HttpStatus.NOT_FOUND, "추천 결과를 찾을 수 없습니다."),
    USER_PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자 정보를 찾을 수 없습니다."),
    STORY_CANDIDATE_NOT_FOUND(HttpStatus.NOT_FOUND, "추천 가능한 동화가 없습니다."),
    INVALID_RECOMMENDATION_SIZE(HttpStatus.BAD_REQUEST, "추천 개수가 올바르지 않습니다."),
    RECOMMENDATION_BUILD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "추천 생성에 실패했습니다.");

    private final HttpStatus status;
    private final String message;
}