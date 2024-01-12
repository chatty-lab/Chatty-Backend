package com.chatty.dto.match.request;

import com.chatty.entity.match.Match;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchRequest {

    @Positive(message = "최소 나이는 양수여야 합니다.")
    private int minAge;

    @Positive(message = "최대 나이는 양수여야 합니다.")
    private int maxAge;

    @NotNull(message = "성별은 필수로 선택해야 합니다.")
    private Gender gender;

    private Double scope;

    @NotBlank(message = "관심사는 필수로 선택해야 합니다.")
    private String category;

    @Builder
    public MatchRequest(final int minAge, final int maxAge, final Gender gender, final Double scope, final String category) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.gender = gender;
        this.scope = scope;
        this.category = category;
    }

    public Match toEntity(final User user, final LocalDateTime now) {
        return Match.builder()
                .minAge(minAge)
                .maxAge(maxAge)
                .category(category)
                .scope(scope)
                .gender(gender)
                .user(user)
                .registeredDateTime(now)
                .build();
    }
}
