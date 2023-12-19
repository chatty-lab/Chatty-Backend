package com.chatty.dto;

import com.chatty.constants.ErrorCode;

public class ErrorResponseDto extends ResponseDto {

    public ErrorResponseDto(ErrorCode errorCode) {
        super(errorCode.getHttpStatus().value(), errorCode.getMessage());
    }

    public static ErrorResponseDto of(ErrorCode errorCode){
        return new ErrorResponseDto(errorCode);
    }
}
