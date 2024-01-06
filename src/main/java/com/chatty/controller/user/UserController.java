package com.chatty.controller.user;

import com.chatty.constants.Code;
import com.chatty.dto.ApiResponse;
import com.chatty.dto.DataResponseDto;
import com.chatty.dto.user.request.*;
import com.chatty.dto.user.response.UserResponse;
import com.chatty.dto.user.response.UserResponseDto;
import com.chatty.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public DataResponseDto<UserResponseDto> login(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[UserController/login] 로그인 시작");
        return DataResponseDto.of(userService.login(userRequestDto));
    }

    @PostMapping("/join")
    public DataResponseDto<UserResponseDto> join(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[UserController/join] 회원 가입 시작");
        return DataResponseDto.of(userService.join(userRequestDto));
    }

    @PutMapping("/update")
    public ApiResponse<UserResponse> joinComplete(@Valid @RequestBody UserJoinRequest request,
                                                  final Authentication authentication) {

        return ApiResponse.ok(userService.joinComplete(authentication.getName(), request));
    }

    @PutMapping("/nickname")
    public ApiResponse<UserResponse> updateNickname(@Valid @RequestBody UserNicknameRequest request,
                                                    final Authentication authentication) {
        return ApiResponse.ok(userService.updateNickname(authentication.getName(), request));
    }

    @PutMapping("/gender")
    public ApiResponse<UserResponse> updateGender(@Valid @RequestBody UserGenderRequest request,
                                                  final Authentication authentication) {
        return ApiResponse.ok(userService.updateGender(authentication.getName(), request));
    }

    @PutMapping("/birth")
    public ApiResponse<UserResponse> updateBirth(@Valid @RequestBody UserBirthRequest request,
                                                 final Authentication authentication) {

        return ApiResponse.ok(userService.updateBirth(authentication.getName(), request));
    }

    @PutMapping("/mbti")
    public ApiResponse<UserResponse> updateMbti(@Valid @RequestBody UserMbtiRequest request,
                                                final Authentication authentication) {

        return ApiResponse.ok(userService.updateMbti(authentication.getName(), request));
    }

    @PutMapping("/coordinate")
    public ApiResponse<UserResponse> updateCoordinate(@Valid @RequestBody UserCoordinateRequest request,
                                                      final Authentication authentication) {

        return ApiResponse.ok(userService.updateCoordinate(authentication.getName(), request));
    }
}
