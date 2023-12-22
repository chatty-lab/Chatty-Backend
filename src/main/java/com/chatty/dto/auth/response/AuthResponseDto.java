package com.chatty.dto.auth.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String accessToken;

    @JsonProperty
    @NotBlank
    private String refreshToken;

    public static AuthResponseDto of(String accessToken, String refreshToken){
        return AuthResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
