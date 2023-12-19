package com.chatty.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {

    @JsonProperty
    private String accessToken;

    @JsonProperty
    private String refreshToken;

    public static AuthResponseDto of(String accessToken, String refreshToken){
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
