package com.chatty.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String errorCode;
    private HttpStatus status;
    private String message;

    public static ErrorResponse of(final String errorCode, final String message){
        return ErrorResponse.builder()
                .errorCode(errorCode)
                .status(HttpStatus.BAD_REQUEST)
                .message(message)
                .build();
    }
}
