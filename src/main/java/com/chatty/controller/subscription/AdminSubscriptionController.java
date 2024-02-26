package com.chatty.controller.subscription;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.subscription.request.SubscriptionCreateRequest;
import com.chatty.dto.subscription.response.SubscriptionResponse;
import com.chatty.service.subscription.SubscriptionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AdminSubscriptionController {

    private final SubscriptionService subScriptionService;

    @PostMapping("/v1/subscription")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<SubscriptionResponse> createSubscription(@Valid @RequestBody SubscriptionCreateRequest request) {
        return ApiResponse.ok(subScriptionService.createSubscription(request));
    }
}
