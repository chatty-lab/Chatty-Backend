package com.chatty.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Code {

    OK(HttpStatus.OK, "응답 성공"),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "올바르지 않은 파라미터 형식입니다."),
    SMS_NOT_SEND(HttpStatus.BAD_GATEWAY, "sms전송을 실패했습니다."),
    INVALID_AUTH_NUMBER(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 번호 입니다."),
    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "이미 존재 하는 유저 입니다."),
    ALREADY_EXIST_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임 입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재 하지 않는 유저 입니다."),
    NOT_SEND_SMS(HttpStatus.BAD_REQUEST, "naver에서 sms전송 실패"),
    NOT_AUTH_NUMBER_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 번호 형식 입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "accessToken 유효성 검증을 실패했습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refreshToken 유효성 검증을 실패했습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refreshToken이 만료되었습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "accessToken이 만료되었습니다."),

    NOT_FOUND_CHAT_ROOM(HttpStatus.BAD_REQUEST, "채팅방이 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}