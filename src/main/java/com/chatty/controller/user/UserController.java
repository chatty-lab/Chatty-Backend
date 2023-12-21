package com.chatty.controller.user;

import com.chatty.constants.Code;
import com.chatty.dto.user.request.UserRequestDto;
import com.chatty.dto.user.response.UserResponseDto;
import com.chatty.service.user.UserService;
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
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[UserController/login] 로그인 시작");
        return ResponseEntity.status(Code.OK.getHttpStatus()).body(userService.login(userRequestDto));
    }

    @PostMapping("/join")
    public ResponseEntity<UserResponseDto> join(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[UserController/join] 회원 가입 시작");
        return ResponseEntity.status(Code.OK.getHttpStatus()).body(userService.join(userRequestDto));
    }
}
