package com.chatty.dto.match.response;

import com.chatty.dto.match.request.MatchRequest;
import com.chatty.entity.match.Match;
import com.chatty.entity.user.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MatchResponse {

    private Long id;
    private Long userId;
    private String nickname;
    private Gender gender;
    private Mbti mbti;
    private String address;
    private String imageUrl;
    private Coordinate coordinate;
    private int age;
    private boolean isSuccess;

    private int requestMinAge;
    private int requestMaxAge;
    private String requestCategory;
    private Double requestScope;
    private Gender requestGender;

    @Builder
    public MatchResponse(final Long id, final Long userId, final String nickname, final Gender gender, final Mbti mbti, final String address, final String imageUrl, final Coordinate coordinate, final int age, final boolean isSuccess, final int requestMinAge, final int requestMaxAge, final String requestCategory, final Double requestScope, final Gender requestGender) {
        this.id = id;
        this.userId = userId;
        this.nickname = nickname;
        this.gender = gender;
        this.mbti = mbti;
        this.address = address;
        this.imageUrl = imageUrl;
        this.coordinate = coordinate;
        this.age = age;
        this.isSuccess = isSuccess;
        this.requestMinAge = requestMinAge;
        this.requestMaxAge = requestMaxAge;
        this.requestCategory = requestCategory;
        this.requestScope = requestScope;
        this.requestGender = requestGender;
    }

    public static MatchResponse of(final Match match, final int age) {
        return MatchResponse.builder()
                .id(match.getId())
                .userId(match.getUser().getId())
                .nickname(match.getUser().getNickname())
                .gender(match.getUser().getGender())
                .mbti(match.getUser().getMbti())
                .address(match.getUser().getAddress())
                .imageUrl(match.getUser().getImageUrl())
                .coordinate(Coordinate.builder()
                        .lat(match.getUser().getLocation().getY())
                        .lng(match.getUser().getLocation().getX())
                        .build())
                .age(age)
                .isSuccess(match.isSuccess())
                .requestScope(match.getScope())
                .requestCategory(match.getCategory())
                .requestGender(match.getGender())
                .requestMinAge(match.getMinAge())
                .requestMaxAge(match.getMaxAge())
                .build();
    }
}
