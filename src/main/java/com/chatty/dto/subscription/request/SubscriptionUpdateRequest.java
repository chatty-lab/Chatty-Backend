package com.chatty.dto.subscription.request;

import com.chatty.entity.subscription.Subscription;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubscriptionUpdateRequest {

    private String name;

    private int duration;

    private int price;

    @Builder
    public SubscriptionUpdateRequest(final String name, final int duration, final int price) {
        this.name = name;
        this.duration = duration;
        this.price = price;
    }
}
