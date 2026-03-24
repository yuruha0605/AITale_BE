package com.example.user.domain.dto.response;

public record SignInResponse(String accessToken, String refreshToken, long accessTokenExpiry,
                             long refreshTokenExpiry) {

}