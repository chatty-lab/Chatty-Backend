package com.chatty.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    @NotBlank(message = "휴대폰 번호 입력은 필수입니다.")
    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "올바른 번호 형식을 입력해주세요.")
    private String mobileNumber;

    @NotBlank(message = "기기 번호 입력은 필수입니다.")
    private String deviceId;

    @NotBlank(message = "sms 인증 번호 입력은 필수입니다.")
    private String authenticationNumber;

    @NotBlank(message = "기기 토큰 입력은 필수입니다.")
    private String deviceToken;
}
