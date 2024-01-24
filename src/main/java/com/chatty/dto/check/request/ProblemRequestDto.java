package com.chatty.dto.check.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProblemRequestDto {

    @NotBlank(message = "휴대폰 번호는 필수로 입력해야 합니다.")
    private String mobileNumber;
}
