package com.chatty.entity.subscription;

import com.chatty.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class UserSubscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;

    private LocalDate startDate;
    private LocalDate endDate;

    @Builder
    public UserSubscription(final User user, final Subscription subscription, final LocalDate startDate, final LocalDate endDate) {
        this.user = user;
        this.subscription = subscription;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
