package com.chatty.utils;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JwtTokenUtilsTest {
    @Test
    @DisplayName("Refresh토큰 랜덤 uuid 생성")
    void createRandomUuid() throws Exception{
        //given
        String mobileNumber = "010-1234-5678";

        //when
        String uuid1 = JwtTokenUtils.getRefreshTokenRandomUuid(mobileNumber);
        String uuid2 = JwtTokenUtils.getRefreshTokenRandomUuid(mobileNumber);

        //then
        Assertions.assertThat(uuid1).isNotEqualTo(uuid2);
    }
}
