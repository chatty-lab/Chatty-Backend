package com.chatty.controller.auth;

import com.chatty.dto.sms.request.MessageDto;
import com.chatty.dto.sms.request.UserSmsRequestDto;
import com.chatty.dto.DataResponseDto;
import com.chatty.dto.ErrorResponseDto;
import com.chatty.dto.ResponseDto;
import com.chatty.exception.NormalException;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.service.auth.AuthService;
import com.chatty.service.sms.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.HashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public DataResponseDto refresh(HttpServletRequest request) {
        HashMap<String, String> newTokens = authService.reissueTokens(jwtTokenProvider.resolveAccessToken(request),
                jwtTokenProvider.resolvRefreshToken(request));
        return DataResponseDto.of(newTokens);
    }

    @PostMapping("/mobile")
    public ResponseDto mobile(@Valid @RequestBody UserSmsRequestDto userSmsRequestDto) throws Exception {
        log.info("번호 인증 요청");
        String authNumber = smsService.saveSms(userSmsRequestDto);
        smsService.sendSms(
                MessageDto.builder().to(userSmsRequestDto.getMobileNumber()).content(authNumber).build());
        return DataResponseDto.of(authNumber);
    }
}
