package com.aitale.analytics.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AnalyticsErrorCode {

    INVALID_STUDY_RESULT(
        HttpStatus.BAD_REQUEST,
        "잘못된 학습 결과 요청입니다."
    ),

    DUPLICATE_STUDY_RESULT(
        HttpStatus.CONFLICT,
        "이미 반영된 학습 결과입니다."
    ),

    GROWTH_SNAPSHOT_NOT_FOUND(
        HttpStatus.NOT_FOUND,
        "사용자 성장 데이터가 없습니다."
    );

    private final HttpStatus status;
    private final String message;
}