package com.chatty.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum Code {

    OK(HttpStatus.OK, "응답 성공",null),
    INVALID_PARAMETER(HttpStatus.BAD_REQUEST, "올바르지 않은 파라미터 형식입니다.","000"),
    INVALID_AUTH_NUMBER(HttpStatus.BAD_REQUEST, "유효하지 않은 인증 번호 입니다.","001"),
    ALREADY_EXIST_USER(HttpStatus.BAD_REQUEST, "이미 존재 하는 유저 입니다.","002"),
    ALREADY_EXIST_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임 입니다.","003"),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재 하지 않는 유저 입니다.","004"),
    NOT_SEND_SMS(HttpStatus.BAD_REQUEST, "naver에서 sms전송 실패","005"),
    NOT_AUTH_NUMBER_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 번호 형식 입니다.","006"),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "accessToken 유효성 검증을 실패했습니다.","007"),
    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refreshToken 유효성 검증을 실패했습니다.","008"),
    EXPIRED_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, "refreshToken이 만료되었습니다.","009"),
    EXPIRED_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "accessToken이 만료되었습니다.","010"),

    NOT_FOUND_CHAT_ROOM(HttpStatus.BAD_REQUEST, "채팅방이 존재하지 않습니다.","011"),
    ALREADY_EXIST_CHATROOM(HttpStatus.BAD_REQUEST, "채팅방이 이미 존재합니다.","012"),
    NOT_FOUND_CHAT_MESSAGE(HttpStatus.BAD_REQUEST, "채팅 내용이 존재하지 않습니다.","013"),
    SMS_NOT_SEND(HttpStatus.BAD_GATEWAY, "sms전송을 실패했습니다.","014");

    private final HttpStatus httpStatus;
    private final String message;
    private final String errorCode;
}