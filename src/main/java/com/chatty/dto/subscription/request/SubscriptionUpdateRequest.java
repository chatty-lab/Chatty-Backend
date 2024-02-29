package com.chatty.dto.subscription.request;

import com.chatty.entity.subscription.Subscription;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SubscriptionUpdateRequest {

    @NotBlank(message = "구독권 이름은 필수로 입력해야 합니다.")
    private String name;

    @Min(value = 30, message = "구독권 기간은 30일 이상이여야 합니다.")
    private int duration;

    @Positive(message = "구독권 가격은 양수여야 합니다.")
    private int price;

    @Builder
    public SubscriptionUpdateRequest(final String name, final int duration, final int price) {
        this.name = name;
        this.duration = duration;
        this.price = price;
    }
}
