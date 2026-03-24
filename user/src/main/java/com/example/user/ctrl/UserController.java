package com.example.user.ctrl;

import com.example.user.domain.dto.request.DifficultyRequestDTO;
import com.example.user.domain.dto.request.LoginRequestDTO;
import com.example.user.domain.dto.request.UserInterestRequestDTO;
import com.example.user.domain.dto.request.UserRequestDTO;
import com.example.user.domain.dto.response.UserResponseDTO;
import com.example.user.service.UserService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User-Service", description = "유저 관련 API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "새로운 유저 회원가입")
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp(@RequestBody UserRequestDTO userRequestDTO) {

        System.out.println(" >>> user ctrl path : /signUp");
        System.out.println(" >>> params : " + userRequestDTO);

        UserResponseDTO userResponseDTO = userService.signUp(
            userRequestDTO); // userResponseDTO : email , password반환

        return ResponseEntity
            .status(HttpStatus.OK).body(userResponseDTO);

    }

    @Operation(summary = "로그인", description = "아이디와 비번으로 로그인")
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody LoginRequestDTO request) {

        System.out.println(" >>> user ctrl path : /signIn");
        System.out.println(" >>> params : " + request);
        // LoginRequestDTO request = LoginRequestDTO.builder()
        // .email(userRequestDTO.getEmail())
        // .password(userRequestDTO.getPassword())
        // .build() ;

        Map<String, Object> map = userService.signIn(request);
        HttpHeaders headers = new HttpHeaders();

        headers.add("Authorization", "Bearer " + (String) (map.get("access"))); // 액세스 토큰
        headers.add("Refresh-Token", (String) (map.get("refresh")));
        headers.add("Access-Control-Expose-Headers", "Authorization, Refresh-Token");

        return ResponseEntity
            .status(HttpStatus.OK)
            .headers(headers)
            .body((String) (map.get("access")));

    }

    // 소연님과 상의 후 고쳐야 함 !!!
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
    @GetMapping("/getProfile")
    public ResponseEntity<?> getProfile(@RequestHeader("X-User-Id") Long userSystemId) {

        System.out.println(" >>> user ctrl path : /getProfile");
        UserResponseDTO userResponseDTO = userService.getProfile(userSystemId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(userResponseDTO);

    }

    // 관심사 설정 (관심사는 순위없음)
    // 입력값 : json배열. 예시 {"interests": [1,2,3]} / 반환값 : List []
    @PostMapping("/createInterest")
    public ResponseEntity<?> createInterest(@RequestHeader("X-User-Id") Long userSystemId,
        @RequestBody UserInterestRequestDTO requestDTO) {
        System.out.println(" >>> user ctrl path : /createInterest");

        List<Long> savedInterests = userService.createInterest(userSystemId, requestDTO);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(savedInterests);
    }

    // 관심사 불러오기
    @PostMapping("/getInterests")
    public ResponseEntity<?> getInterests(@RequestHeader("X-User-Id") Long userSystemId) {
        System.out.println(" >>> user ctrl path : /user/getInterests");
        List<Long> interests = userService.getInterests(userSystemId);

        return ResponseEntity
            .status(HttpStatus.OK)
            .body(interests);

    }

    // 사용자 레벨 증가
    // 경험치(EXP)가 쌓이면 사용자 레벨이 올라감. --> openFeign방식으로 한다고 가정함.
    @PostMapping("/internal/increaseLevelUp")
    public ResponseEntity<?> updateLevel(@RequestBody Long userSystemId) {

        int updateLevel = userService.increaseUserLevel(userSystemId);
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(updateLevel);

    }
}
