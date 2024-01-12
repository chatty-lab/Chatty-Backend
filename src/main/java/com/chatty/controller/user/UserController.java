package com.chatty.controller.user;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.user.request.*;
import com.chatty.dto.user.response.UserResponse;
import com.chatty.dto.user.response.UserResponseDto;
import com.chatty.service.user.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @Operation(summary = "로그인", description = "로그인에 필요한 정보를 입력한 후, 로그인을 완료합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입 실패",
            content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "E-004", value = """
                                    {
                                        "errorCode": "004",
                                        "status": "400",
                                        "message": "존재하지 않는 유저 입니다."
                                    }
                                    """),
                            @ExampleObject(name = "E-003", value = """
                                    {
                                        "errorCode": "001",
                                        "status": "400",
                                        "message": "유효하지 않은 인증 번호 입니다."
                                    }
                                    """),
                    }
            )
    )
    @PostMapping("/login")
    public ApiResponse<UserResponseDto> login(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[UserController/login] 로그인 시작");
        return ApiResponse.ok(userService.login(userRequestDto));
    }


    @Operation(summary = "회원가입", description = "sms인증으로 1차 회원가입을 완료합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입 실패",
            content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "E-001", value = """
                                    {
                                        "errorCode": "001",
                                        "status": "400",
                                        "message": "유효하지 않은 인증 번호입니다."
                                    }
                                    """),
                            @ExampleObject(name = "E-003", value = """
                                    {
                                        "errorCode": "002",
                                        "status": "400",
                                        "message": "이미 존재하는 유저 입니다."
                                    }
                                    """),
                    }
            )
    )
    @PostMapping("/join")
    public ApiResponse<UserResponseDto> join(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.info("[UserController/join] 회원 가입 시작");
        return ApiResponse.ok(userService.join(userRequestDto));
    }

    @Operation(summary = "최종 회원가입", description = "회원가입에 필요한 정보를 입력한 후, 회원가입을 완료합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입 실패",
            content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "E-004", value = """
                                    {
                                        "errorCode": "004",
                                        "status": "400",
                                        "message": "존재하지 않는 유저 입니다."
                                    }
                                    """),
                            @ExampleObject(name = "E-003", value = """
                                    {
                                        "errorCode": "003",
                                        "status": "400",
                                        "message": "이미 존재하는 닉네임입니다."
                                    }
                                    """)
                    }
            )
    )
    @PutMapping("/update")
    public ApiResponse<UserResponse> joinComplete(@Valid @RequestBody UserJoinRequest request,
                                                  final Authentication authentication) {

        return ApiResponse.ok(userService.joinComplete(authentication.getName(), request));
    }

    @Operation(summary = "닉네임 변경", description = "닉네임을 변경합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입 실패",
            content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "E-003", value = """
                                    {
                                        "errorCode": "003",
                                        "status": "400",
                                        "message": "이미 존재하는 닉네임입니다."
                                    }
                                    """),
                    }
            )
    )
    @PutMapping("/nickname")
    public ApiResponse<UserResponse> updateNickname(@Valid @RequestBody UserNicknameRequest request,
                                                    final Authentication authentication) {
        return ApiResponse.ok(userService.updateNickname(authentication.getName(), request));
    }

    @Hidden
    @PutMapping("/gender")
    public ApiResponse<UserResponse> updateGender(@Valid @RequestBody UserGenderRequest request,
                                                  final Authentication authentication) {
        return ApiResponse.ok(userService.updateGender(authentication.getName(), request));
    }

    @Hidden
    @PutMapping("/birth")
    public ApiResponse<UserResponse> updateBirth(@Valid @RequestBody UserBirthRequest request,
                                                 final Authentication authentication) {

        return ApiResponse.ok(userService.updateBirth(authentication.getName(), request));
    }

    @Operation(summary = "MBTI 변경", description = "MBTI를 변경합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입 실패",
            content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "E-004", value = """
                                    {
                                        "errorCode": "004",
                                        "status": "400",
                                        "message": "존재하지 않는 유저 입니다."
                                    }
                                    """),
                    }
            )
    )
    @PutMapping("/mbti")
    public ApiResponse<UserResponse> updateMbti(@Valid @RequestBody UserMbtiRequest request,
                                                final Authentication authentication) {

        return ApiResponse.ok(userService.updateMbti(authentication.getName(), request));
    }

    @Hidden
    @Operation(summary = "주소 변경", description = "주소를 변경합니다.")
    @PutMapping("/coordinate")
    public ApiResponse<UserResponse> updateCoordinate(@Valid @RequestBody UserCoordinateRequest request,
                                                      final Authentication authentication) {

        return ApiResponse.ok(userService.updateCoordinate(authentication.getName(), request));
    }

    @PutMapping("/image")
    public ApiResponse<UserResponse> updateImage(@RequestParam("image") MultipartFile image,
                                                 final Authentication authentication) throws IOException {
        return ApiResponse.ok(userService.updateImage(authentication.getName(), image));
    }
}
