package com.chatty.entity.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @DisplayName("닉네임을 변경한다.")
    @Test
    void updateNickname() {
        // given
        User user = createUser();

        // when
        user.updateNickname("닉네임수정");

        // then
        assertThat(user.getNickname()).isEqualTo("닉네임수정");

    }

    private User createUser() {
        return User.builder()
                .nickname("닉네임")
                .build();
    }
}