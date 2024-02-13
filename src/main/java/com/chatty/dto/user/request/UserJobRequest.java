package com.chatty.dto.user.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserJobRequest {

    private String job;

    @Builder
    public UserJobRequest(final String job) {
        this.job = job;
    }
}
