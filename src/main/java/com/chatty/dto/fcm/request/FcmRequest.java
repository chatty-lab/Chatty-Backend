package com.chatty.dto.fcm.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FcmRequest {

    private Long userId;
    private String title;
    private String body;

    @Builder
    public FcmRequest(final Long userId, final String title, final String body) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }
}
