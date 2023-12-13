package com.chatty.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthenticationNumberUtilTest {
    @Test
    @DisplayName("인증번호 랜덤 생성 테스트")
    void createAuthenticationNumber() throws Exception{
        //given
        AuthenticationNumberUtil authenticationNumberUtil = new AuthenticationNumberUtil();
        //when
        String number = authenticationNumberUtil.generateNumber();

        //then
        Assertions.assertThat(number.length()).isEqualTo(6);
    }
}
