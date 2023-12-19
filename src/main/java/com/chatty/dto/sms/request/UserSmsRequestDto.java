package com.chatty.dto.sms.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserSmsRequestDto {

    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String uuid;
}
