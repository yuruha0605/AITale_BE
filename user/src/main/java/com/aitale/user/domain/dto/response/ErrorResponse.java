package com.aitale.user.domain.dto.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private String message;
    private LocalDateTime timestamp;
    private String path;

    public static ErrorResponse of(int status, String message, String path) {
        return ErrorResponse.builder()
            .status(status)
            .message(message)
            .timestamp(LocalDateTime.now())
            .path(path)
            .build();
    }
}
