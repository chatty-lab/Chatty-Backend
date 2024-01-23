package com.chatty.service.match;

import com.chatty.dto.match.request.MatchRequest;
import com.chatty.dto.match.response.MatchResponse;
import com.chatty.entity.match.Match;
import com.chatty.constants.Authority;
import com.chatty.entity.user.Coordinate;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.match.MatchRepository;
import com.chatty.repository.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

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
        LocalDate birth = LocalDate.of(1999, 1, 1);
        int calculateAge = LocalDate.now().getYear() - birth.getYear() + 1;

        User user = createUser("박지성", "01012345678", birth, Gender.MALE, false);
        userRepository.save(user);

        MatchRequest request = MatchRequest.builder()
                .minAge(25)
                .maxAge(30)
                .scope(300.0)
                .category("category")
                .gender(Gender.FEMALE)
                .blueCheck(false)
                .build();

        // when
        MatchResponse matchResponse = matchService.createMatch(user.getMobileNumber(), request);

        // then
        assertThat(matchResponse.getId()).isNotNull();
        assertThat(matchResponse)
                .extracting("nickname", "gender", "age", "requestMinAge", "requestMaxAge")
                .containsExactlyInAnyOrder("박지성", Gender.MALE, calculateAge, 25, 30);
    }

    @DisplayName("매칭이 완료되었을 때, isSuccess 값이 true로 바뀐다.")
    @Test
    void updateIsSuccess() {
        // given
        LocalDate birth = LocalDate.of(2000, 1, 1);
        User user = createUser("박지성", "01022222222", birth, Gender.MALE, false);
        userRepository.save(user);

        Match match = createMatch(user, LocalDateTime.now(), 25, 30, 30.0, Gender.FEMALE, false);
        matchRepository.save(match);

        // when
        MatchResponse matchResponse = matchService.successMatch(match.getId());

        // then
        assertThat(matchResponse.isSuccess()).isTrue();
    }

    @DisplayName("남성은 하루 매칭 제한 횟수 5번을 초과하였을 때, 예외가 발생한다.")
    @Test
    void createMatchWithLimitExceededForMale() {
        // given
        LocalDate birth = LocalDate.of(2000, 1, 1);
        User user = createUser("박지성", "01022222222", birth, Gender.MALE, false);
        userRepository.save(user);

        LocalDateTime localDateTime = LocalDateTime.now();
        Match match1 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.FEMALE, true);
        Match match2 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.FEMALE, true);
        Match match3 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.FEMALE, true);
        Match match4 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.FEMALE, true);
        Match match5 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.FEMALE, true);
        matchRepository.saveAll(List.of(match1, match2, match3, match4, match5));

        MatchRequest request = MatchRequest.builder()
                .minAge(25)
                .maxAge(30)
                .scope(300.0)
                .category("category")
                .gender(Gender.FEMALE)
                .blueCheck(false)
                .build();

        // when // then
        assertThatThrownBy(() -> matchService.createMatch(user.getMobileNumber(), request))
                .isInstanceOf(CustomException.class)
                .hasMessage("일일 매칭 횟수 제한을 초과했습니다.");
    }

    @DisplayName("여성은 하루 매칭 제한 횟수 10번을 초과하였을 때, 예외가 발생한다.")
    @Test
    void createMatchWithLimitExceededForFemale() {
        // given
        LocalDate birth = LocalDate.of(2000, 1, 1);
        User user = createUser("김연아", "01033333333", birth, Gender.FEMALE, false);
        userRepository.save(user);

        LocalDateTime localDateTime = LocalDateTime.now();
        Match match1 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.MALE, true);
        Match match2 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.MALE, true);
        Match match3 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.MALE, true);
        Match match4 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.MALE, true);
        Match match5 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.MALE, true);
        Match match6 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.MALE, true);
        Match match7 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.MALE, true);
        Match match8 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.MALE, true);
        Match match9 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.MALE, true);
        Match match10 = createMatch(user, localDateTime, 25, 30, 300.0, Gender.MALE, true);
        matchRepository.saveAll(List.of(match1, match2, match3, match4, match5, match6, match7, match8, match9, match10));

        MatchRequest request = MatchRequest.builder()
                .minAge(25)
                .maxAge(30)
                .scope(300.0)
                .category("category")
                .gender(Gender.MALE)
                .blueCheck(false)
                .build();

        // when // then
        assertThatThrownBy(() -> matchService.createMatch(user.getMobileNumber(), request))
                .isInstanceOf(CustomException.class)
                .hasMessage("일일 매칭 횟수 제한을 초과했습니다.");
    }

    @DisplayName("프로필 인증(BlueCheck)을 받은 사람만 매칭을 시작할 때, blueCheck 기능을 사용할 수 있다.")
    @Test
    void createMatchWithBlueCheck() {
        // given
        User user = createUser("박지성", "01012345678", LocalDate.now(), Gender.MALE, true);
        userRepository.save(user);

        MatchRequest request = MatchRequest.builder()
                .minAge(25)
                .maxAge(30)
                .scope(300.0)
                .category("category")
                .gender(Gender.FEMALE)
                .blueCheck(true)
                .build();

        // when
        MatchResponse matchResponse = matchService.createMatch(user.getMobileNumber(), request);

        // then
        assertThat(matchResponse.isRequestBlueCheck()).isTrue();
        assertThat(matchResponse.isBlueCheck()).isTrue();
    }

    @DisplayName("프로필 인증(BlueCheck)이 없는 사람이 매칭을 시작할 때, blueCheck 기능을 사용하면 예외가 발생한다.")
    @Test
    void createMatchWithoutBlueCheck() {
        // given
        User user = createUser("박지성", "01012345678", LocalDate.now(), Gender.MALE, false);
        userRepository.save(user);

        MatchRequest request = MatchRequest.builder()
                .minAge(25)
                .maxAge(30)
                .scope(300.0)
                .category("category")
                .gender(Gender.FEMALE)
                .blueCheck(true)
                .build();

        // when // then
        assertThatThrownBy(() -> matchService.createMatch(user.getMobileNumber(), request))
                .isInstanceOf(CustomException.class)
                .hasMessage("프로필 인증이 되어있지 않습니다.");
    }

    private User createUser(final String nickname, final String mobileNumber, final LocalDate birth, final Gender gender, final boolean blueCheck) {
        return User.builder()
                .mobileNumber(mobileNumber)
                .deviceId("123456")
                .authority(Authority.USER)
//                .mbti(Mbti.ENFJ)
                .birth(birth)
                .imageUrl("이미지")
                .address("주소")
                .gender(gender)
                .nickname(nickname)
                .location(User.createPoint(
                        Coordinate.builder()
                                .lat(37.1)
                                .lng(127.1)
                                .build()
                ))
                .blueCheck(blueCheck)
                .build();
    }

    private Match createMatch(final User user, final LocalDateTime localDateTime, final int minAge, final int maxAge, final double scope, final Gender gender, final boolean matchStatus) {
        return Match.builder()
                .user(user)
                .minAge(minAge)
                .maxAge(maxAge)
                .scope(scope)
                .gender(gender)
                .category("category")
                .registeredDateTime(localDateTime)
                .isSuccess(matchStatus)
                .build();
    }
}