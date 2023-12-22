package com.chatty.dto.user.request;

import com.chatty.entity.user.Gender;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserGenderRequest {

    @NotNull(message = "성별은 필수로 선택해야 됩니다.")
    private Gender gender;

    @Builder
    public UserGenderRequest(final Gender gender) {
        this.gender = gender;
    }
}
