package com.chatty.service.profileUnlock;

import com.chatty.constants.Authority;
import com.chatty.dto.profileUnlock.request.ProfileUnlockRequest;
import com.chatty.dto.profileUnlock.response.ProfileUnlockResponse;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.ProfileUnlock;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.profileUnlock.ProfileUnlockRepository;
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
class ProfileUnlockServiceTest {

    @Autowired
    private ProfileUnlockService profileUnlockService;

    @Autowired
    private ProfileUnlockRepository profileUnlockRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() {
        profileUnlockRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("캔디를 이용하여 프로필 잠금을 해제한다.")
    @Test
    void unlockProfile() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User unlocker = createUser("박지성", "01012345678", 7);
        User unlockedUser = createUser("강혜원", "01011112222", 7);
        userRepository.saveAll(List.of(unlocker, unlockedUser));

        ProfileUnlockRequest request = ProfileUnlockRequest.builder()
                .unlockMethod("candy")
                .build();

        // when
        ProfileUnlockResponse profileUnlockResponse = profileUnlockService.unlockProfile(unlockedUser.getId(), unlocker.getMobileNumber(), request, now);

        // then
        assertThat(profileUnlockResponse.getId()).isNotNull();
        assertThat(profileUnlockResponse)
                .extracting("unlockerId", "unlockedUserId", "registeredDateTime")
                .containsExactlyInAnyOrder(unlocker.getId(), unlockedUser.getId(), now);
    }

    @DisplayName("캔디를 이용하여 프로필 잠금을 해제할 때, 보유한 캔디(7개미만)가 부족하면 예외가 발생한다.")
    @Test
    void unlockProfileWithNoCandy() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User unlocker = createUser("박지성", "01012345678", 6);
        User unlockedUser = createUser("강혜원", "01011112222", 7);
        userRepository.saveAll(List.of(unlocker, unlockedUser));

        ProfileUnlockRequest request = ProfileUnlockRequest.builder()
                .unlockMethod("candy")
                .build();

        // when // then
        assertThatThrownBy(() -> profileUnlockService.unlockProfile(unlockedUser.getId(), unlocker.getMobileNumber(), request, now))
                .isInstanceOf(CustomException.class)
                .hasMessage("캔디의 개수가 부족합니다.");
    }

    @DisplayName("캔디를 이용하여 프로필 잠금을 해제할 때, 이미 잠금을 해제한 유저면 예외가 발생한다.")
    @Test
    void unlockProfileWithDuplicateUnlock() {
        // given
        LocalDateTime now = LocalDateTime.now();
        User unlocker = createUser("박지성", "01012345678", 6);
        User unlockedUser = createUser("강혜원", "01011112222", 7);
        userRepository.saveAll(List.of(unlocker, unlockedUser));

        ProfileUnlock profileUnlock = ProfileUnlock.builder()
                .registeredDateTime(now)
                .unlocker(unlocker)
                .unlockedUser(unlockedUser)
                .build();
        profileUnlockRepository.save(profileUnlock);

        ProfileUnlockRequest request = ProfileUnlockRequest.builder()
                .unlockMethod("candy")
                .build();

        // when // then
        assertThatThrownBy(() -> profileUnlockService.unlockProfile(unlockedUser.getId(), unlocker.getMobileNumber(), request, now))
                .isInstanceOf(CustomException.class)
                .hasMessage("이미 프로필 잠금을 해제했습니다.");
    }

    private User createUser(final String nickname, final String mobileNumber, final int candy) {
        return User.builder()
                .mobileNumber(mobileNumber)
                .deviceId("123456")
                .authority(Authority.USER)
                .birth(LocalDate.now())
                .imageUrl("이미지")
                .address("주소")
                .gender(Gender.MALE)
                .nickname(nickname)
                .candy(candy)
                .build();
    }

}