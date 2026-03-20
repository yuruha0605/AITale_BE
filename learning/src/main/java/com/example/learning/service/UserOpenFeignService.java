package com.example.learning.service;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "http://user-service")
public interface UserOpenFeignService {

    @PutMapping("/users/{userId}/assigned-difficulty")
    void updateAssignedDifficulty(@PathVariable("userId") Long userId,
            @RequestBody Map<String, String> body);

}
