package com.chatty.service.subscription;

import com.chatty.dto.subscription.request.SubscriptionCreateRequest;
import com.chatty.dto.subscription.response.SubscriptionResponse;
import com.chatty.repository.subscription.SubscriptionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
}