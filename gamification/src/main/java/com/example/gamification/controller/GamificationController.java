package com.example.gamification.controller;

import com.example.gamification.dto.RewardRequest;
import com.example.gamification.service.GamificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.gamification.domain.UserExpStatus;
import com.example.gamification.domain.UserBadge;
import java.util.List;

@Tag(name = "Gamification-Service", description = "게임화 및 보상 시스템 API")
@RestController
@RequestMapping("/api/gamification")
@RequiredArgsConstructor // 생성자 주입을 자동으로 해줍니다.
public class GamificationController {

    private final GamificationService gamificationService;

    // 보상 처리 API (경험치 추가 + 로그 저장 + 배지 체크)
    @Operation(summary = "보상 처리", description = "사용자의 보상을 처리하고 경험치를 추가합니다")
    @PostMapping("/reward")
    public ResponseEntity<String> processReward(@RequestBody RewardRequest request) {
        gamificationService.processReward(request);
        return ResponseEntity.ok("보상 처리가 성공적으로 완료되었습니다.");
    }

    // 1. 사용자의 현재 경험치 및 레벨 정보 조회
    @Operation(summary = "사용자 경험치 상태 조회", description = "사용자의 현재 경험치 및 레벨 정보를 조회합니다")
    @GetMapping("/status/{userId}")
    public ResponseEntity<UserExpStatus> getUserStatus(@PathVariable Long userId) {
        UserExpStatus status = gamificationService.getUserStatus(userId);
        return ResponseEntity.ok(status);
    }

    // 2. 사용자가 획득한 배지 목록 조회
    @Operation(summary = "사용자 배지 조회", description = "사용자가 획득한 배지 목록을 조회합니다")
    @GetMapping("/badges/{userId}")
    public ResponseEntity<List<UserBadge>> getUserBadges(@PathVariable Long userId) {
        List<UserBadge> badges = gamificationService.getUserBadges(userId);
        return ResponseEntity.ok(badges);
    }
}