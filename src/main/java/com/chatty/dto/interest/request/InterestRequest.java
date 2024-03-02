package com.chatty.dto.interest.request;

import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class InterestRequest {

    @Size(min = 3, message = "관심사는 최소 3개 선택해야 됩니다.")
    private List<Long> interests;

    @Builder
    public InterestRequest(final List<Long> interests) {
        this.interests = interests;
    }
}
