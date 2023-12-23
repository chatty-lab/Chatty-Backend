package com.chatty.dto.user.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserBirthRequest {

    @NotNull(message = "생년월일은 필수로 입력해야 됩니다.")
    private LocalDate birth;

    @Builder
    public UserBirthRequest(final LocalDate birth) {
        this.birth = birth;
    }
}
