package com.aitale.user.ctrl;

import com.aitale.user.domain.dto.request.DifficultyRequestDTO;
import com.aitale.user.domain.dto.request.LoginRequestDTO;
import com.aitale.user.domain.dto.request.UserInterestRequestDTO;
import com.aitale.user.domain.dto.request.UserRequestDTO;
import com.aitale.user.domain.dto.response.UserResponseDTO;
import com.aitale.user.provider.JwtProvider;
import com.aitale.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User-Service", description = "유저 관련 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "회원가입", description = "새로운 유저 회원가입")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody UserRequestDTO userRequestDTO) {

        System.out.println(" >>> user ctrl path : /signup");
        System.out.println(" >>> params : " + userRequestDTO);

        UserResponseDTO userResponseDTO = userService.signUp(userRequestDTO);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userResponseDTO);
    }

    @Operation(summary = "로그인", description = "아이디와 비번으로 로그인")
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody LoginRequestDTO request) {

        System.out.println(" >>> user ctrl path : /signin");
        System.out.println(" >>> params : " + request);
        // LoginRequestDTO request = LoginRequestDTO.builder()
        // .email(userRequestDTO.getEmail())
        // .password(userRequestDTO.getPassword())
        // .build() ;

        Map<String, Object> map = userService.signIn(request);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + (String) map.get("access"));
        headers.add("Refresh-Token", (String) map.get("refresh"));
        headers.add("Access-Control-Expose-Headers", "Authorization, Refresh-Token");

        return ResponseEntity
            .status(HttpStatus.OK)
            .headers(headers)
            .body(Map.of(
                "accessToken", map.get("access"),
                "refreshToken", map.get("refresh")
            ));

    }

    // TODO: 소연님과 상의 후 고쳐야 함 !!!
    // 난이도 저장
    @PostMapping("/internal/createAssignedDifficulty")
    public ResponseEntity<?> createAssignedDifficulty(
        @RequestBody DifficultyRequestDTO difficultyRequestDTO) {

        System.out.println(" >>> user ctrl path : /createAssignedDifficulty");

        String difficulty = difficultyRequestDTO.getDifficulty();
        Long userSystemId = difficultyRequestDTO.getUserSystemId();
        userService.createAssignedDifficulty(userSystemId, difficulty);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(difficulty);

    }

    // 프로필(이메일) 불러오기
    @GetMapping("/getEmail")
    public ResponseEntity<?> getEmail(@RequestHeader("X-User-Id") Long userSystemId) {

        String email = userService.getEmail(userSystemId);

        return ResponseEntity.ok(Map.of("email", email));
    }

    // 프로필 (이메일, 나이, 레벨, 난이도) 불러오기
    @GetMapping("/me/profile")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {

        System.out.println(" >>> user ctrl path : /me/profile");
        Long userSystemId = jwtProvider.getUserIdFromToken(token);

        UserResponseDTO userResponseDTO = userService.getProfile(userSystemId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userResponseDTO);
    }

    // 관심사 설정 (관심사는 순위없음)
    // 입력값 : json배열. 예시 {"interests": [1,2,3]} / 반환값 : List []
    @PutMapping("/me/interests")
    public ResponseEntity<?> createInterest(
        @RequestHeader("Authorization") String token,
        @RequestBody UserInterestRequestDTO requestDTO) {

        System.out.println(" >>> user ctrl path : /me/interests");
        Long userSystemId = jwtProvider.getUserIdFromToken(token);

        List<Long> savedInterests = userService.createInterest(userSystemId, requestDTO);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(savedInterests);
    }

    // 관심사 불러오기
    @GetMapping("/me/interests")
    public ResponseEntity<?> getInterests(@RequestHeader("Authorization") String token) {
        System.out.println(" >>> user ctrl path : /me/interests");
        Long userSystemId = jwtProvider.getUserIdFromToken(token);

        List<Long> interests = userService.getInterests(userSystemId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(interests);
    }
}