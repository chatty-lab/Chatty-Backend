package com.chatty.controller.user;

import com.chatty.constants.Code;
import com.chatty.dto.ApiResponse;
import com.chatty.dto.DataResponseDto;
import com.chatty.dto.user.request.*;
import com.chatty.dto.user.response.UserResponse;
import com.chatty.dto.user.response.UserResponseDto;
import com.chatty.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
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

    @Operation(summary = "로그인", description = "로그인에 필요한 정보를 입력한 후, 로그인을 완료합니다.")
    @PostMapping("/login")
    public DataResponseDto<UserResponseDto> login(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[UserController/login] 로그인 시작");
        return DataResponseDto.of(userService.login(userRequestDto));
    }

    @Operation(summary = "회원가입", description = "sms인증으로 1차 회원가입을 완료합니다.")
    @PostMapping("/join")
    public DataResponseDto<UserResponseDto> join(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[UserController/join] 회원 가입 시작");
        return DataResponseDto.of(userService.join(userRequestDto));
    }

    @Operation(summary = "최종 회원가입", description = "회원가입에 필요한 정보를 입력한 후, 회원가입을 완료합니다.")
    @PutMapping("/update")
    public ApiResponse<UserResponse> joinComplete(@Valid @RequestBody UserJoinRequest request,
                                                  final Authentication authentication) {

        return ApiResponse.ok(userService.joinComplete(authentication.getName(), request));
    }

    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다.")
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

    @Operation(summary = "MBTI 변경", description = "MBTI를 변경합니다.")
    @PutMapping("/mbti")
    public ApiResponse<UserResponse> updateMbti(@Valid @RequestBody UserMbtiRequest request,
                                                final Authentication authentication) {

        return ApiResponse.ok(userService.updateMbti(authentication.getName(), request));
    }

    @Operation(summary = "주소 변경", description = "주소를 변경합니다.")
    @PutMapping("/coordinate")
    public ApiResponse<UserResponse> updateCoordinate(@Valid @RequestBody UserCoordinateRequest request,
                                                      final Authentication authentication) {

        return ApiResponse.ok(userService.updateCoordinate(authentication.getName(), request));
    }
}
