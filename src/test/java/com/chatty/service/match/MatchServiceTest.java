package com.chatty.service.match;

import com.chatty.dto.match.request.MatchRequest;
import com.chatty.dto.match.response.MatchResponse;
import com.chatty.entity.user.Authority;
import com.chatty.entity.user.Coordinate;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import com.chatty.repository.match.MatchRepository;
import com.chatty.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MatchServiceTest {

    @Autowired
    private MatchService matchService;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        matchRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("유저가 매칭을 시작한다.")
    @Test
    void createMatch() {
        // given
        LocalDate birth = LocalDate.of(2000, 1, 1);
        User user = createUser("박지성", "01012345678", birth);
        userRepository.save(user);

        MatchRequest request = MatchRequest.builder()
                .minAge(25)
                .maxAge(30)
                .scope(300.0)
                .category("category")
                .gender(Gender.FEMALE)
                .build();

        // when
        MatchResponse matchResponse = matchService.createMatch(user.getMobileNumber(), request);

        // then
        assertThat(matchResponse.getId()).isNotNull();
        assertThat(matchResponse)
                .extracting("nickname", "gender", "age", "requestMinAge", "requestMaxAge")
                .containsExactlyInAnyOrder("박지성", Gender.MALE, 25, 25, 30);
    }

    private User createUser(final String nickname, final String mobileNumber, final LocalDate birth) {
        return User.builder()
                .mobileNumber(mobileNumber)
                .uuid("123456")
                .authority(Authority.USER)
//                .mbti(Mbti.ENFJ)
                .birth(birth)
                .imageUrl("이미지")
                .address("주소")
                .gender(Gender.MALE)
                .nickname(nickname)
                .location(User.createPoint(
                        Coordinate.builder()
                                .lat(37.1)
                                .lng(127.1)
                                .build()
                ))
                .build();
    }
}