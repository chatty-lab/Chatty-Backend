package com.chatty.dto.interest.response;

import com.chatty.entity.user.Interest;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class InterestResponse {

    private Long id;

    private String name;

    @Builder
    public InterestResponse(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public static InterestResponse of(final Interest interest) {
        return InterestResponse.builder()
                .id(interest.getId())
                .name(interest.getName())
                .build();
    }
}
