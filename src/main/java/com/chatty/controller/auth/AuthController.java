package com.chatty.controller.auth;

import com.chatty.dto.DataResponseDto;
import com.chatty.dto.auth.request.AuthRequestDto;
import com.chatty.dto.auth.response.AuthResponseDto;
import com.chatty.dto.sms.request.UserSmsRequestDto;
import com.chatty.dto.sms.response.SmsUserResponseDto;
import com.chatty.service.auth.AuthService;
import com.chatty.service.sms.SmsService;
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

    @PostMapping("/refresh")
    public DataResponseDto<AuthResponseDto> refresh(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return DataResponseDto.of(authService.reissueTokens(authRequestDto));
    }

    @PostMapping("/sms")
    public DataResponseDto<SmsUserResponseDto> mobile(@Valid @RequestBody UserSmsRequestDto userSmsRequestDto) throws Exception {
        log.info("번호 인증 요청");
        return DataResponseDto.of(smsService.saveSms(userSmsRequestDto));
    }
}
