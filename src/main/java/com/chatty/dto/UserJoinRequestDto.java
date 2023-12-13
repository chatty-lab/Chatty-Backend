package com.chatty.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserJoinRequestDto {
    @NotBlank
    private String mobileNumber;

    @NotBlank
    private String uuid;

    @NotBlank
    private String username;

    @NotBlank
    private String nickname;

    @NotBlank
    private String birth;

    @NotBlank
    private String gender;

    private String mbti;

    private String address;

    private Double latitude;

    private Double longitude;

    @NotBlank
    private String authority;
}
