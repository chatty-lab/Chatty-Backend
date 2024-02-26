package com.chatty.service.subscription;

import com.chatty.dto.subscription.request.SubscriptionCreateRequest;
import com.chatty.dto.subscription.request.SubscriptionUpdateRequest;
import com.chatty.dto.subscription.response.SubscriptionResponse;
import com.chatty.entity.subscription.Subscription;
import com.chatty.repository.subscription.SubscriptionRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class SubscriptionServiceTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private SubscriptionService subscriptionService;

    @AfterEach
    void tearDown() {
        subscriptionRepository.deleteAllInBatch();
    }

    @DisplayName("새로운 구독권을 등록한다.")
    @Test
    void createSubscription() {
        // given
        SubscriptionCreateRequest request = SubscriptionCreateRequest.builder()
                .name("구독권1")
                .duration(30)
                .price(9_900)
                .build();

        // when
        SubscriptionResponse subscriptionResponse = subscriptionService.createSubscription(request);

        // then
        assertThat(subscriptionResponse.getId()).isNotNull();
        assertThat(subscriptionResponse)
                .extracting("name", "price", "duration")
                .containsExactlyInAnyOrder("구독권1", 9_900, 30);
    }
    
    @DisplayName("등록된 구독권을 수정한다.")
    @Test
    void updateSubscription() {
        // given
        Subscription subscription = createSubscription("구독권1", 9_900, 30);
        subscriptionRepository.save(subscription);

        SubscriptionUpdateRequest request = SubscriptionUpdateRequest.builder()
                .name("수정된 구독권")
                .duration(60)
                .price(18_900)
                .build();

        // when
        SubscriptionResponse subscriptionResponse = subscriptionService.updateSubscription(request, subscription.getId());

        // then
        assertThat(subscriptionResponse.getId()).isNotNull();
        assertThat(subscriptionResponse)
                .extracting("name", "price", "duration")
                .containsExactlyInAnyOrder("수정된 구독권", 18_900, 60);
    }

    @DisplayName("등록된 구독권을 삭제한다.")
    @Test
    void deleteSubscription() {
        // given
        Subscription subscription = createSubscription("구독권1", 9_900, 30);
        subscriptionRepository.save(subscription);

        // when
        subscriptionService.deleteSubscription(subscription.getId());

        // then
        assertThatThrownBy(() -> subscriptionRepository.findById(subscription.getId()).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    private Subscription createSubscription(final String name, final int price, final int duration) {
        return Subscription.builder()
                .name(name)
                .price(price)
                .duration(duration)
                .build();
    }
}