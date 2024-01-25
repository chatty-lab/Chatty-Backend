package com.chatty.dto.check.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CheckRequestDto {

    @NotBlank(message = "휴대폰 번호는 필수로 입력해야 합니다.")
    private String mobileNumber;

    @NotBlank(message = "정답은 필수로 입력해야 합니다.")
    private String answer;
}
