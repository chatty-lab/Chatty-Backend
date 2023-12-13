package com.chatty.controller;

import com.chatty.dto.UserRequestDto;
import com.chatty.service.UserService;
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
    public ResponseEntity<String> login(@Valid @RequestBody UserRequestDto userRequestDto){
        log.info("[UserController/login] 로그인 시작");
        String token = userService.login(userRequestDto);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody UserRequestDto userRequestDto){
        log.info("[UserController/join] 회원 가입 시작");
        String token = userService.join(userRequestDto);
        return ResponseEntity.ok().body(token);
    }
}
