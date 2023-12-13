package com.chatty.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserRequestDto {

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String uuid;

    @NotBlank
    private String authenticationNumber;
}
