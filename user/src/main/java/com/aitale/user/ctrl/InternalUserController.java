package com.aitale.user.ctrl;

import com.aitale.user.domain.dto.request.DifficultyRequestDTO;
import com.aitale.user.domain.dto.response.InternalUserProfileResponse;
import com.aitale.user.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User-Service Internal", description = "유저 내부 연동 API")
@RestController
@RequestMapping("/internal/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;

    // recommendation 등 내부 서비스용 이메일 조회
    @GetMapping("/{userSystemId}/email")
    public ResponseEntity<?> getEmail(@PathVariable Long userSystemId) {
        String email = userService.getEmail(userSystemId);
        return ResponseEntity.ok(Map.of("email", email));
    }

    // recommendation 등 내부 서비스용 프로필 조회
    @GetMapping("/{userSystemId}/profile")
    public ResponseEntity<InternalUserProfileResponse> getProfile(@PathVariable Long userSystemId) {

        System.out.println(" >>> user ctrl path : /internal/users/{userSystemId}/profile");

        InternalUserProfileResponse response = userService.getInternalProfile(userSystemId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response);
    }

    // 내부 서비스용 관심사 조회
    @GetMapping("/{userSystemId}/interests")
    public ResponseEntity<?> getInterests(@PathVariable Long userSystemId) {
        System.out.println(" >>> user ctrl path : /internal/users/{userSystemId}/interests");
        List<Long> interests = userService.getInterests(userSystemId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(interests);
    }

    // TODO: 소연님과 상의 후 고쳐야 함 !!!
    // 난이도 저장
    @PatchMapping("/{userSystemId}/difficulty")
    public ResponseEntity<?> createAssignedDifficulty(
        @PathVariable Long userSystemId,
        @RequestBody DifficultyRequestDTO difficultyRequestDTO) {

        System.out.println(" >>> user ctrl path : /internal/users/{userSystemId}/difficulty");

        String difficulty = difficultyRequestDTO.getDifficulty();
        userService.createAssignedDifficulty(userSystemId, difficulty);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Map.of("difficulty", difficulty));
    }

    // 사용자 레벨 증가
    // 경험치(EXP)가 쌓이면 사용자 레벨이 올라감. --> openFeign방식으로 한다고 가정함.
    @PatchMapping("/{userSystemId}/level")
    public ResponseEntity<?> updateLevel(@PathVariable Long userSystemId) {

        int updateLevel = userService.increaseUserLevel(userSystemId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(Map.of("currentLevel", updateLevel));
    }
}