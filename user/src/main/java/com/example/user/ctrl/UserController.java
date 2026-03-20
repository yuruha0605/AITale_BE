package com.example.user.ctrl;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.user.domain.dto.LoginRequestDTO;
import com.example.user.domain.dto.UserRequestDTO;
import com.example.user.domain.dto.UserResponseDTO;
import com.example.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "User-Service", description = "유저 관련 API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService ;
    @Operation(summary = "회원가입", description = "새로운 유저 회원가입")
    @PostMapping("/signUp")
    public ResponseEntity<?> signUp (@RequestBody UserRequestDTO userRequestDTO) {

      System.out.println(" >>> user ctrl path : /signUp") ;
      System.out.println(" >>> params : " + userRequestDTO) ;

      UserResponseDTO userResponseDTO = userService.signUp(userRequestDTO) ; // userResponseDTO : email , password반환

      return ResponseEntity
                .status(HttpStatus.OK).body(userResponseDTO) ;

    }

    @Operation(summary = "로그인", description = "아이디와 비번으로 로그인")
    @PostMapping("/signIn")
    public ResponseEntity<?> signIn (@RequestBody LoginRequestDTO request) {

      System.out.println(" >>> user ctrl path : /signIn") ;
      System.out.println(" >>> params : " + request) ;
      // LoginRequestDTO request = LoginRequestDTO.builder()
      //                           .email(userRequestDTO.getEmail())
      //                           .password(userRequestDTO.getPassword())
      //                           .build() ;

      Map<String, Object> map = userService.signIn(request) ;
      HttpHeaders headers = new HttpHeaders() ;

      headers.add("Authorization", "Bearer "+(String)(map.get("access")) ) ; // 액세스 토큰
      headers.add("Refresh-Token", (String)(map.get("refresh")) ) ;
      headers.add("Access-Control-Expose-Headers","Authorization, Refresh-Token");
    
      return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body( (String)(map.get("access")) ) ;

  }

  // 난이도 테스트 후 데이터베이스에 저장하는 컨트롤러
  @PostMapping("/assignedDifficulty")
  public ResponseEntity<?> assignedDifficulty (int score) {

    System.out.println(" >>> user ctrl path : /assignedDifficulty") ;
    System.out.println(" >>> score : " + score) ;
    
    userService.assignedDifficulty(score) ;
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(score) ;

  }
  
}
