package com.chatty.entity.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ProfileUnlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unlocker_id")
    private User unlocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unlocked_user_id")
    private User unlockedUser;

    private LocalDateTime registeredDateTime;

    @Builder
    public ProfileUnlock(final User unlocker, final User unlockedUser, final LocalDateTime registeredDateTime) {
        this.unlocker = unlocker;
        this.unlockedUser = unlockedUser;
        this.registeredDateTime = registeredDateTime;
    }
}
