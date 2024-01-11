package com.chatty.dto;

import com.chatty.constants.Code;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String errorCode;
    private String message;

    public static ErrorResponse of(Code code){
        return ErrorResponse.builder()
                .errorCode(code.getErrorCode())
                .message(code.getMessage())
                .build();
    }

    public static ErrorResponse of(final String message){
        return ErrorResponse.builder()
                .message(message)
                .build();
    }
}
