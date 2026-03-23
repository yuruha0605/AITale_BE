package com.aitale.analytics.exception;

import lombok.Getter;

@Getter
public class AnalyticsException extends RuntimeException {

  private final AnalyticsErrorCode errorCode;

  public AnalyticsException(AnalyticsErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
  }
}
