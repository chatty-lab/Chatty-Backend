package com.chatty.dto.interest.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InterestRequest {

    private List<Long> interests;

    @Builder
    public InterestRequest(final List<Long> interests) {
        this.interests = interests;
    }
}
