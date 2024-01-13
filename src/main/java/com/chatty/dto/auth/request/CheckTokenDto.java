package com.chatty.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CheckTokenDto {
    @NotBlank
    private String accessToken;
}
