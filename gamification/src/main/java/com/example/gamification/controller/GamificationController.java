package com.example.gamification.controller;

import com.example.gamification.dto.RewardRequest;
import com.example.gamification.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gamification")
@RequiredArgsConstructor
public class GamificationController {

    private final GamificationService gamificationService;

    @PostMapping("/reward")
    public String grantReward(@RequestBody RewardRequest request) {
        gamificationService.processReward(request);
        return "보상이 성공적으로 처리되었습니다.";
    }
}
