package com.aitale.user.domain.dto.response;

public record SignInResponse(String accessToken, String refreshToken, long accessTokenExpiry,
                             long refreshTokenExpiry) {

}