package com.chatty.dto.subscription.response;

import com.chatty.entity.subscription.Subscription;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubscriptionResponse {

    private Long id;

    private String name;

    private int duration;

    private int price;

    @Builder
    public SubscriptionResponse(final Long id, final String name, final int duration, final int price) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    public static SubscriptionResponse of(final Subscription subscription) {
        return SubscriptionResponse.builder()
                .id(subscription.getId())
                .name(subscription.getName())
                .duration(subscription.getDuration())
                .price(subscription.getPrice())
                .build();
    }
}
