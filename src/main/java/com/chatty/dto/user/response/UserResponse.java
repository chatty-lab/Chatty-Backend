package com.chatty.dto.user.response;

import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserResponse {

    private Long id;
    private String mobileNumber;
    private String nickname;
    private LocalDate birth;
    private Gender gender;
    private String mbti;
    private String address;
    private String authority;
    private String imageUrl;

    @Builder
    public UserResponse(final Long id, final String mobileNumber, final String nickname, final LocalDate birth, final Gender gender, final String mbti, final String address, final String authority, final String imageUrl) {
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.nickname = nickname;
        this.birth = birth;
        this.gender = gender;
        this.mbti = mbti;
        this.address = address;
        this.authority = authority;
        this.imageUrl = imageUrl;
    }

    public static UserResponse of(final User user) {
        return UserResponse.builder()
                .id(user.getId())
                .mobileNumber(user.getMobileNumber())
                .nickname(user.getNickname())
                .birth(user.getBirth())
                .gender(user.getGender())
                .mbti(user.getMbti())
                .address(user.getAddress())
                .authority(user.getAuthority().name())
                .imageUrl(user.getImageUrl())
                .build();
    }
}
