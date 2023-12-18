package com.chatty.controller;

import com.chatty.dto.request.UserSmsRequestDto;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.service.AuthService;
import com.chatty.service.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SmsService smsService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request){
        HashMap<String,String> newTokens = authService.reissueTokens(jwtTokenProvider.resolveAccessToken(request),
                jwtTokenProvider.resolvRefreshToken(request));

        return ResponseEntity.ok().body(newTokens.toString());
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("test");
        return ResponseEntity.ok().body("test");
    }

    @PostMapping("/mobile")
    public ResponseEntity<String> mobile(@Valid @RequestBody UserSmsRequestDto userSmsRequestDto) throws Exception {
        log.info("번호 인증 요청");
        String authNumber = smsService.saveSms(userSmsRequestDto);
        return ResponseEntity.ok().body("인증 번호 : " + authNumber);
    }
}
