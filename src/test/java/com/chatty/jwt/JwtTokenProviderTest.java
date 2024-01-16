package com.chatty.jwt;

import static org.assertj.core.api.Assertions.*;

import com.chatty.repository.token.RefreshTokenRepository;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import com.chatty.utils.JwtTokenUtils;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    private static final String secretKey = "asdfdsfsdfdsfdssdfsdfsfsfsfsfsfdsfsfsfssdsfssdffsf";
    private static SecretKey key;
    private final long accessTokenExpirationTime = 3000;
    private final long refreshTokenExpirationTime = 5000;
    private final String PREFIX_ACCESTOKEN = "Bearer ";

    @BeforeAll
    public static void initSecretKey() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(jwtTokenProvider, "key", key);
        ReflectionTestUtils.setField(jwtTokenProvider, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtTokenProvider, "ACCESS_TOKEN_EXPIRED_TIME", accessTokenExpirationTime);
        ReflectionTestUtils.setField(jwtTokenProvider, "REFRESH_TOKEN_EXPIRED_TIME", refreshTokenExpirationTime);
    }

    @Test
    @DisplayName("액세스 토큰 생성하는 경우")
    void createAccessToken() throws Exception {
        //given
        String mobileNumber = "01012345678";
        String uuid = "abc";

        //when
        String accessToken = jwtTokenProvider.createAccessToken(mobileNumber, uuid);

        //then
        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("리프레시 토큰 생성하는 경우")
    void createRefreshToken() throws Exception {
        //given
        String mobileNumber = "01012345678";
        String uuid = "abc";

        //when
        String accessToken = jwtTokenProvider.createRefreshToken(mobileNumber, uuid);

        //then
        assertThat(accessToken).isNotNull();
    }

    @Test
    @DisplayName("accessToken에서 mobile번호 추출하는 경우")
    void getMobileNumber() throws Exception {
        //given
        String mobileNumber1 = "01012345678";
        String uuid = "abc";
        String accessToken = PREFIX_ACCESTOKEN + jwtTokenProvider.createAccessToken(mobileNumber1, uuid);

        //when
         String mobileNumber2 = jwtTokenProvider.getMobileNumber(JwtTokenUtils.getAccessToken(accessToken));

        //then
        assertThat(mobileNumber1).isEqualTo(mobileNumber2);
    }

    @Test
    @DisplayName("refreshToken에서 uuid 추출하는 경우")
    void getUuidByRefreshToken() throws Exception {
        //given
        String mobileNumber1 = "01012345678";
        String uuid = "abc";
        String key = mobileNumber1 + " " + uuid;
        String refreshToken = jwtTokenProvider.createRefreshToken(mobileNumber1, uuid);

        //when
        String uuid2 = jwtTokenProvider.getDeviceIdByRefreshToken(refreshToken);

        //then
        assertThat(key).isEqualTo(uuid2);
    }

    @Test
    @DisplayName("토큰이 존재하는 경우")
    void isExistToken() throws Exception {
        //given
        String token = "asd";
        //when
        boolean result = jwtTokenProvider.isExistToken(token);
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("토큰이 올바른 형식인 경우")
    void isRightFormat() throws Exception {
        //given
        String token = "Bearer asdfsfsdafasfdsafdsfsfsafasfdsfsddfasfafsda";
        //when
        boolean result = jwtTokenProvider.isRightFormat(token);
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("토큰이 유효한 경우")
    void isValidToken() throws Exception {
        //given
        String mobileNumber = "01012345678";
        String uuid = "123";
        String token = jwtTokenProvider.createAccessToken(mobileNumber, uuid);
        //when
        boolean result = jwtTokenProvider.isValidToken(token);
        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("토큰 유효시간이 만료된 경우")
    void isExpiredToken() throws Exception {
        //given
        String mobileNumber = "01012345678";
        String uuid = "123";
        ReflectionTestUtils.setField(jwtTokenProvider, "ACCESS_TOKEN_EXPIRED_TIME", 0);

        String token = jwtTokenProvider.createAccessToken(mobileNumber, uuid);

        //when
        boolean result = jwtTokenProvider.isExpiredToken(token);

        //then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("토큰 유효시간이 만료되지 않은 경우")
    void isNotExpiredToken() throws Exception {
        //given
        String mobileNumber = "01012345678";
        String uuid = "123";

        ReflectionTestUtils.setField(jwtTokenProvider, "ACCESS_TOKEN_EXPIRED_TIME", 3000);

        String token = jwtTokenProvider.createAccessToken(mobileNumber, uuid);

        //when
        boolean result = jwtTokenProvider.isExpiredToken(token);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("레디스에 저장된 refreshToken과 일치하는 경우")
    void isEqualRedisRefresh() throws Exception {
        //given
        String token = "abcdefg";
        String uuid = "abc";
        refreshTokenRepository.save("abc", "abcdefg");
        //when
        boolean result = jwtTokenProvider.isEqualRedisRefresh(token, uuid);
        //then
        assertThat(result).isTrue();
    }
}
