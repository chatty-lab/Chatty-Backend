package com.chatty.controller.auth;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.auth.request.AuthRequestDto;
import com.chatty.dto.auth.response.AuthResponseDto;
import com.chatty.dto.sms.request.UserSmsRequestDto;
import com.chatty.dto.sms.response.SmsUserResponseDto;
import com.chatty.service.auth.AuthService;
import com.chatty.service.sms.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final SmsService smsService;

    @Operation(summary = "토큰 재발급", description = "refreshToken을 사용해서 토큰을 재발급합니다.")
    @PostMapping("/refresh")
    public ApiResponse<AuthResponseDto> refresh(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return ApiResponse.ok(authService.reissueTokens(authRequestDto));
    }

    @Operation(summary = "번호 인증 요청", description = "전화번호로 sms 인증요청을 합니다.")
    @PostMapping("/mobile")
    public ApiResponse<SmsUserResponseDto> mobile(@Valid @RequestBody UserSmsRequestDto userSmsRequestDto) throws Exception {
        log.info("번호 인증 요청");
        return ApiResponse.ok(smsService.saveSms(userSmsRequestDto));
    }
}
