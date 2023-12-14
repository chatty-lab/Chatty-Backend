package com.chatty.controller;

import com.chatty.jwt.JwtTokenProvider;
import com.chatty.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(HttpServletRequest request, HttpServletResponse response){
        HashMap<String,String> newTokens = authService.reissueTokens(jwtTokenProvider.resolveAccessToken(request),
                jwtTokenProvider.resolvRefreshToken(request));

        return ResponseEntity.ok().body(newTokens.toString());
    }
}
