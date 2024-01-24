package com.chatty.dto.check.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckCompleteResponseDto {
    private String accessToken;
    private String refreshToken;

    public static CheckCompleteResponseDto of(String accessToken, String refreshToken){
        return CheckCompleteResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
