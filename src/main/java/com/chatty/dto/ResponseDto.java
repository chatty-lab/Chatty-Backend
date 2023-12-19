package com.chatty.dto;

import com.chatty.constants.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class ResponseDto {

    private final Integer code;
    private final String message;

    public static ResponseDto of(ErrorCode errorCode){
        return new ResponseDto(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }
}
