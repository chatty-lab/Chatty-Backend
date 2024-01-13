package com.chatty.dto.auth.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CheckTokenDto {
    @NotBlank(message = "accessToken은 필수로 입력해야 합니다.")
    private String accessToken;
}
