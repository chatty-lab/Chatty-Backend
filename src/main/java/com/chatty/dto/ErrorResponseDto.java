package com.chatty.dto;

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

    public static ErrorResponseDto of(String message){
        return ErrorResponseDto.builder()
                .message(message).build();
    }
}
