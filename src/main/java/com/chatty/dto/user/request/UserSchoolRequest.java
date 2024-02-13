package com.chatty.dto.user.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSchoolRequest {

    private String school;

    @Builder
    public UserSchoolRequest(final String school) {
        this.school = school;
    }
}
