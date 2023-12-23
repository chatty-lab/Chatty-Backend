package com.chatty.dto.user.request;

import com.chatty.entity.user.Mbti;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserMbtiRequest {

    @NotNull(message = "MBTI는 필수로 입력해야 됩니다.")
    private Mbti mbti;

    @Builder
    public UserMbtiRequest(final Mbti mbti) {
        this.mbti = mbti;
    }
}
