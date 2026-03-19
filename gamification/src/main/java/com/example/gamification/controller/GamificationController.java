package com.example.gamification.controller;

import com.example.gamification.dto.RewardRequest;
import com.example.gamification.service.GamificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gamification")
@RequiredArgsConstructor // 생성자 주입을 자동으로 해줍니다.
public class GamificationController {

    private final GamificationService gamificationService;

    // 보상 처리 API (경험치 추가 + 로그 저장 + 배지 체크)
    @PostMapping("/reward")
    public ResponseEntity<String> processReward(@RequestBody RewardRequest request) {
        gamificationService.processReward(request);
        return ResponseEntity.ok("보상 처리가 성공적으로 완료되었습니다.");
    }
}