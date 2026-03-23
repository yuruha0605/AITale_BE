package com.aitale.analytics.exception;

import com.aitale.analytics.common.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AnalyticsExceptionHandler {

  @ExceptionHandler(AnalyticsException.class)
  public ResponseEntity<ApiResponse<Void>> handleAnalyticsException(AnalyticsException e) {
    AnalyticsErrorCode errorCode = e.getErrorCode();

    return ResponseEntity.status(errorCode.getStatus())
        .body(ApiResponse.fail(errorCode.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiResponse<Void>> handleValidationException(
      MethodArgumentNotValidException e) {
    String message = e.getBindingResult().getFieldErrors().stream()
        .findFirst()
        .map(error -> error.getField() + ": " + error.getDefaultMessage())
        .orElse("잘못된 요청입니다.");

    return ResponseEntity.badRequest().body(ApiResponse.fail(message));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ApiResponse<Void>> handleConstraintException(
      ConstraintViolationException e) {
    return ResponseEntity.badRequest().body(ApiResponse.fail(e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
    return ResponseEntity.internalServerError().body(ApiResponse.fail("서버 내부 오류가 발생했습니다."));
  }
}
