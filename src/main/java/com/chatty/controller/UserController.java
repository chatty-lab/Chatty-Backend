package com.chatty.controller;

import com.chatty.dto.UserJoinRequestDto;
import com.chatty.dto.UserLoginRequestDto;
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
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto){
        String token = userService.login(userLoginRequestDto);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@Valid @RequestBody UserJoinRequestDto userJoinRequestDto){
        log.info("[UserController/join] 회원가입 컨트롤러 시작");
        String token = userService.join(userJoinRequestDto);
        return ResponseEntity.ok().body(token);
    }
}
