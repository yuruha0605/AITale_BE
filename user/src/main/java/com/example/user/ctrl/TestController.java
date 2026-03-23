package com.example.user.ctrl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// google auth 로그인을 위한 설정
@RestController
public class TestController {

    @GetMapping("/test-login")
    public String testLogin(@RequestParam("token") String token) {
        return "구글 로그인 성공! 발급된 JWT: " + token;
    }
}