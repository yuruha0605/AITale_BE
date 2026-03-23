package com.aitale.recommendation.infrastructure;

import com.aitale.recommendation.dto.response.UserProfileResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/internal/users/{userId}/profile")
    UserProfileResponse getUserProfile(@PathVariable Long userId);
}
