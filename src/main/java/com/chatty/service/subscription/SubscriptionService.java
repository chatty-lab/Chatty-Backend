package com.chatty.service.subscription;

import com.chatty.dto.subscription.request.SubscriptionCreateRequest;
import com.chatty.dto.subscription.response.SubscriptionResponse;
import com.chatty.entity.subscription.Subscription;
import com.chatty.repository.subscription.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public SubscriptionResponse createSubscription(final SubscriptionCreateRequest request) {
        Subscription subscription = subscriptionRepository.save(request.toEntity());

        return SubscriptionResponse.of(subscription);
    }
}
