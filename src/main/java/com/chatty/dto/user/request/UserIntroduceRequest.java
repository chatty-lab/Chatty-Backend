package com.chatty.dto.user.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserIntroduceRequest {

    private String introduce;

    @Builder
    public UserIntroduceRequest(final String introduce) {
        this.introduce = introduce;
    }
}
