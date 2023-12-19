package com.chatty.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    OK(HttpStatus.OK, "응답 성공"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "잘못된 파라미터 입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "잘못된 권한 입니다."),
    AUTH_NUMBER_ERROR(HttpStatus.BAD_REQUEST, "잘못된 인증 번호 입니다."),
    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "이미 존재 하는 유저 입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재 하지 않는 유저 입니다."),
    NOT_AUTH_NUMBER_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 번호 형식 입니다."),
    NOT_VALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "유효 하지 않은 accessToken 입니다."),
    NOT_VALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "유효 하지 않은 refreshToken 입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
