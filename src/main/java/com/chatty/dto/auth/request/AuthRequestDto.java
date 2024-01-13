package com.chatty.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthRequestDto {
    @NotBlank(message = "accessToken은 필수로 입력해야 합니다.")
    private String refreshToken;
}
