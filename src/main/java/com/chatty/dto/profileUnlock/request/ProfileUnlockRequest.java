package com.chatty.dto.profileUnlock.request;

import com.chatty.entity.user.ProfileUnlock;
import com.chatty.entity.user.User;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileUnlockRequest {

//    @AssertTrue(message = "true 값만 들어올 수 있습니다.")
//    private Boolean unlock;
    @NotBlank(message = "candy또는 ticket을 입력해주세요.")
    private String unlockMethod;

    @Builder
    public ProfileUnlockRequest(final String unlockMethod) {
        this.unlockMethod = unlockMethod;
    }

    public ProfileUnlock toEntity(final User unlocker, final User unlockedUser, LocalDateTime registeredDateTime) {
        return ProfileUnlock.builder()
                .unlocker(unlocker)
                .unlockedUser(unlockedUser)
                .registeredDateTime(registeredDateTime)
                .build();
    }

}
