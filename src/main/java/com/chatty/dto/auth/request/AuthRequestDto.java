package com.chatty.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AuthRequestDto {
    @NotBlank
    private String refreshToken;
}
