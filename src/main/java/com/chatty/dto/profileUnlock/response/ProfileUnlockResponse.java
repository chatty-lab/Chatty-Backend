package com.chatty.dto.profileUnlock.response;

import com.chatty.entity.user.ProfileUnlock;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileUnlockResponse {

    private Long id;
    private Long unlockerId;
    private Long unlockedUserId;
    private LocalDateTime localDateTime;

    @Builder
    public ProfileUnlockResponse(final Long id, final Long unlockerId, final Long unlockedUserId, final LocalDateTime localDateTime) {
        this.id = id;
        this.unlockerId = unlockerId;
        this.unlockedUserId = unlockedUserId;
        this.localDateTime = localDateTime;
    }

    public static ProfileUnlockResponse of(final ProfileUnlock profileUnlock) {
        return ProfileUnlockResponse.builder()
                .id(profileUnlock.getId())
                .unlockerId(profileUnlock.getUnlocker().getId())
                .unlockedUserId(profileUnlock.getUnlockedUser().getId())
                .localDateTime(profileUnlock.getLocalDateTime())
                .build();
    }
}
