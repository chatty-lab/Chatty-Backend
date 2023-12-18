package com.chatty.dto.request;

import lombok.Getter;

@Getter
public class UserSmsRequestDto {

    private String mobileNumber;

    private String uuid;
}
