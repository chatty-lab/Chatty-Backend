package com.chatty.controller;

import com.chatty.dto.UserLoginRequestDto;
import com.chatty.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto){
        String mobileNumber = userLoginRequestDto.getMobileNumber();
        String uuid = userLoginRequestDto.getUuid();
        String token = userService.login(mobileNumber,uuid);
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(){
        return ResponseEntity.ok().body("token");
    }
}
