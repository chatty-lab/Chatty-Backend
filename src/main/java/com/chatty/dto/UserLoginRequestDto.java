package com.chatty.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserLoginRequestDto {

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String uuid;
}
