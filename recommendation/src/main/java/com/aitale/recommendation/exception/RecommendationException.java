package com.aitale.recommendation.exception;

import lombok.Getter;

@Getter
public class RecommendationException extends RuntimeException {

    private final RecommendationErrorCode errorCode;

    public RecommendationException(RecommendationErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}