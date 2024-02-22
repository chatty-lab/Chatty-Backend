package com.chatty.repository.profileUnlock;

import com.chatty.constants.Authority;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.ProfileUnlock;
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

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ProfileUnlockRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileUnlockRepository profileUnlockRepository;

    @DisplayName("ProfileUnlock 엔티티에서 1번 유저가 2번 유저를 잠금 해제했는지 확인한다.")
    @Test
    void existsByUnlockerIdAndUnlockedUserId() {
        // given
        User user1 = createUser("박지성", "01012345678", Gender.MALE);
        User user2 = createUser("김연아", "01099998888", Gender.FEMALE);
        userRepository.saveAll(List.of(user1, user2));

        LocalDateTime now = LocalDateTime.now();
        ProfileUnlock profileUnlock = ProfileUnlock.builder()
                .unlocker(user1)
                .unlockedUser(user2)
                .registeredDateTime(now)
                .build();

        profileUnlockRepository.save(profileUnlock);

        // when
        boolean result = profileUnlockRepository.existsByUnlockerIdAndUnlockedUserId(user1.getId(), user2.getId());

        // then
        assertThat(result).isTrue();
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