package com.chatty.dto.user.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserAddressRequest {

    private String address;

    @Builder
    public UserAddressRequest(final String address) {
        this.address = address;
    }
}
