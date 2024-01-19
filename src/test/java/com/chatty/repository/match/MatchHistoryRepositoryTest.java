package com.chatty.repository.match;

import com.chatty.constants.Authority;
import com.chatty.constants.Code;
import com.chatty.entity.match.Match;
import com.chatty.entity.match.MatchHistory;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
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

import static com.chatty.entity.user.Gender.FEMALE;
import static com.chatty.entity.user.Gender.MALE;
import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class MatchHistoryRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchHistoryRepository matchHistoryRepository;

    @DisplayName("매치히스토리에서 유저 2명의 ID를 이용하여 매칭된 적이 있는지 조회한다.")
    @Test
    void findBySenderIdAndReceiverId() {
        // given
        User male = createUser("박지성", "01011111111", MALE);
        User female = createUser("김연아", "01022222222", FEMALE);
        userRepository.saveAll(List.of(male, female));

        MatchHistory matchHistoryRequest = MatchHistory.builder()
                .sender(male)
                .receiver(female)
                .build();
        matchHistoryRepository.save(matchHistoryRequest);

        // when
        boolean matchHistory = matchHistoryRepository.existsBySenderIdAndReceiverId(male.getId(), female.getId());

        // then
        assertThat(matchHistory).isTrue();
    }

    private User createUser(final String nickname, final String mobileNumber, final Gender gender) {
        return User.builder()
                .mobileNumber(mobileNumber)
                .deviceId("123123")
                .authority(Authority.USER)
                .deviceToken("123123")
                .birth(LocalDate.now())
                .imageUrl("이미지")
                .address("주소")
                .gender(gender)
                .nickname(nickname)
                .build();
    }
}