package com.chatty.dto.sms.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsUserResponseDto {
    private String authNumber;

    public static SmsUserResponseDto of(String authNumber){
        return SmsUserResponseDto.builder()
                .authNumber(authNumber)
                .build();
    }
}
