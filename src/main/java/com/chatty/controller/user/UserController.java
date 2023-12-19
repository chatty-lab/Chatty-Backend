package com.chatty.controller.user;

import com.chatty.dto.user.request.UserRequestDto;
import com.chatty.dto.DataResponseDto;
import com.chatty.dto.ResponseDto;
import com.chatty.service.user.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseDto login(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[UserController/login] 로그인 시작");
        Map<String, String> tokens = userService.login(userRequestDto);
        return DataResponseDto.of(tokens);
    }

    @PostMapping("/join")
    public ResponseDto join(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[UserController/join] 회원 가입 시작");
        Map<String, String> tokens = userService.join(userRequestDto);
        return DataResponseDto.of(tokens);
    }
}
