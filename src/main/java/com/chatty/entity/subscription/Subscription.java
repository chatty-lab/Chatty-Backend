package com.chatty.entity.subscription;

import com.chatty.dto.subscription.request.SubscriptionUpdateRequest;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int duration;

    private int price;

    @Builder
    public Subscription(final String name, final int duration, final int price) {
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    public void updateSubscription(final SubscriptionUpdateRequest request) {
        this.name = request.getName();
        this.duration = request.getDuration();
        this.price = request.getPrice();
    }
}
