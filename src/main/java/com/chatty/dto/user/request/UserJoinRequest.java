package com.chatty.dto.user.request;

import com.chatty.constants.Authority;
import com.chatty.entity.user.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserJoinRequest {

    @Schema(description = "좌표")
    @NotNull(message = "좌표는 필수로 입력해야 합니다.")
    private Coordinate coordinate;

    @Schema(description = "닉네임")
    @NotBlank(message = "닉네임은 필수로 입력해야 합니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
    private String nickname;

    @Schema(description = "성별")
    @NotNull(message = "성별은 필수로 선택해야 됩니다.")
    private Gender gender;

    @Schema(description = "생년월일")
    @NotNull(message = "생년월일은 필수로 입력해야 됩니다.")
    private LocalDate birth;

    @Schema(description = "MBTI")
    @NotNull(message = "MBTI는 필수로 입력해야 됩니다.")
    private Mbti mbti;

    @Builder
    public UserJoinRequest(final Coordinate coordinate, final String nickname, final Gender gender, final LocalDate birth, final Mbti mbti) {
        this.coordinate = coordinate;
        this.nickname = nickname;
        this.gender = gender;
        this.birth = birth;
        this.mbti = mbti;
    }

    public User toEntity() {
        return User.builder()
                .nickname(nickname)
                .gender(gender)
                .birth(birth)
                .mbti(mbti)
                .location(User.createPoint(coordinate))
                .authority(Authority.USER)
                .build();
    }
}
