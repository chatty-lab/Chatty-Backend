package com.chatty.jwt;

import com.chatty.entity.RefreshToken;
import com.chatty.repository.RefreshTokenRepository;
import com.chatty.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TYPE = "Bearer ";
    private static final String MOBILE_NUMBER = "mobileNumber";
    private static final String REFRESH_TOKEN = "Refresh";
    private static final String UUID = "uuid";

    @Value("${jwt-secret-key}")
    private String secretKey;

    @Value("${jwt-access-token-expiration-time}")
    private long ACCESS_TOKEN_EXPIRED_TIME;

    @Value("${jwt-refresh-token-expiration-time}")
    private long REFRESH_TOKEN_EXPIRED_TIME;

    private SecretKey key;

    private final RefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String getMobileNumber(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(JwtTokenUtils.getAccessToken(token)).getPayload()
                .get(MOBILE_NUMBER, String.class);
    }

    public String createAccessToken(String mobileNumber, String uuid) {

        Claims claims = Jwts.claims()
                .add(MOBILE_NUMBER, mobileNumber)
                .add(UUID, uuid)
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED_TIME))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String mobileNumber) {

        Claims claims = Jwts.claims()
                .add(UUID, JwtTokenUtils.getRefreshTokenRandomUuid(mobileNumber))
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED_TIME))
                .signWith(key)
                .compact();
    }

    public boolean isExistToken(final String token) {
        if (token == null || token.isBlank()) {
            log.error("token이 존재 하지 않습니다.");
            return false;
        }

        return true;
    }

    public boolean isRightFormat(final String token) {

        if (!token.startsWith(BEARER_TYPE)) {
            log.error("형식에 맞지 않은 token 발송");
            return false;
        }
        return true;
    }

    public boolean isExpiredToken(String token) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration().
                before(new Date());
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION);

        return accessToken;
    }

    public String resolvRefreshToken(HttpServletRequest request){
        String refreshToken = request.getHeader(REFRESH_TOKEN);

        return refreshToken;
    }
}
