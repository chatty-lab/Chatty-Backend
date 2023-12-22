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
public class ErrorResponseDto {
    private String message;

    public static ErrorResponseDto of(Code code){
        return ErrorResponseDto.builder()
                .message(code.getMessage())
                .build();
    }
}
