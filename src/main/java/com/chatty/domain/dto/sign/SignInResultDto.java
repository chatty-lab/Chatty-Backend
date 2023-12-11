package com.chatty.domain.dto.sign;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@ToString
public class SignInResultDto extends SignUpResultDto{

    @Builder
    public SignInResultDto(boolean success, int code, String msg, String token){
        super(success, code, msg, token);
    }
}
