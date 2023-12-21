package com.chatty.controller.auth;

import com.chatty.constants.Code;
import com.chatty.dto.DataResponseDto;
import com.chatty.dto.auth.response.AuthResponseDto;
import com.chatty.dto.sms.request.UserSmsRequestDto;
import com.chatty.dto.sms.response.SmsUserResponseDto;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.service.auth.AuthService;
import com.chatty.service.sms.SmsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<DataResponseDto<AuthResponseDto>> refresh(HttpServletRequest request) {

        return ResponseEntity.status(Code.OK.getHttpStatus()).body(DataResponseDto.of(authService.reissueTokens(jwtTokenProvider.resolveAccessToken(request),
                jwtTokenProvider.resolvRefreshToken(request))));
    }

    @PostMapping("/mobile")
    public ResponseEntity<DataResponseDto<SmsUserResponseDto>> mobile(@Valid @RequestBody UserSmsRequestDto userSmsRequestDto) throws Exception {
        log.info("번호 인증 요청");

        return ResponseEntity.status(Code.OK.getHttpStatus()).body(DataResponseDto.of(smsService.saveSms(userSmsRequestDto)));
    }
}
