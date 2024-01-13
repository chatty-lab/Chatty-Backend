package com.chatty.repository.match;

import com.chatty.constants.Authority;
import com.chatty.entity.match.Match;
import com.chatty.entity.user.Coordinate;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import com.chatty.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class MatchRepositoryTest {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("입력한 유저의 현재 날짜(일) 00:00 부터 다음 날짜(일) 00:00 까지의 매치(성공 or 실패) 리스트를 조회한다.")
    @Test
    void findMatchesBy() {
        // given
        User user = createUser("박지성", "01012345678");
        User user2 = createUser("박지성", "01012345678");
        userRepository.saveAll(List.of(user, user2));

        LocalDateTime now = LocalDateTime.of(2024, 1, 13, 0, 0);
        Match match1 = createMatch(user, LocalDateTime.of(2024, 1, 13, 23, 23), true);
        Match match2 = createMatch(user, LocalDateTime.of(2024, 1, 13, 11, 59), true);
        Match match3 = createMatch(user, LocalDateTime.of(2024, 1, 13, 0, 59), true);
        Match match4 = createMatch(user, LocalDateTime.of(2024, 1, 14, 0, 0), true);
        Match match5 = createMatch(user, LocalDateTime.of(2024, 1, 13, 23, 59), false);
        Match match6 = createMatch(user2, LocalDateTime.of(2024, 1, 13, 23, 59), true);
        matchRepository.saveAll(List.of(match1, match2, match3, match4, match5, match6));

        // when
        Long matchesCountWithTrue = matchRepository.countMatchesBy(user.getId(), now, LocalDateTime.of(2024, 1, 14, 0, 0), true);

        // then
        assertThat(matchesCountWithTrue).isEqualTo(3);

        System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
        System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
        System.out.println("LocalDateTime.now() = " + LocalDateTime.now());
    }

    private User createUser(final String nickname, final String mobileNumber) {
        return User.builder()
                .mobileNumber(mobileNumber)
                .uuid("123456")
                .authority(Authority.USER)
//                .mbti(Mbti.ENFJ)
                .birth(LocalDate.now())
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

    private Match createMatch(final User user, final LocalDateTime localDateTime, final boolean isSuccess) {
        return Match.builder()
                .user(user)
                .registeredDateTime(localDateTime)
                .isSuccess(isSuccess)
                .build();
    }
}