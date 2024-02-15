package com.chatty.dto.profileUnlock.request;

import com.chatty.entity.user.ProfileUnlock;
import com.chatty.entity.user.User;
import jakarta.validation.constraints.AssertTrue;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileUnlockRequest {

    @AssertTrue(message = "true 값만 들어올 수 있습니다.")
    private Boolean unlock;

    @Builder
    public ProfileUnlockRequest(final Boolean unlock) {
        this.unlock = unlock;
    }

    public ProfileUnlock toEntity(final User unlocker, final User unlockedUser, LocalDateTime localDateTime) {
        return ProfileUnlock.builder()
                .unlocker(unlocker)
                .unlockedUser(unlockedUser)
                .localDateTime(localDateTime)
                .build();
    }

}
