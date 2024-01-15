package com.chatty.dto.user.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeviceTokenRequest {

    @NotBlank(message = "deviceToken은 필수로 입력해야 합니다.")
    private String deviceToken;
}
