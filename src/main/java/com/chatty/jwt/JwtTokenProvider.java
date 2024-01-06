package com.chatty.jwt;

import com.chatty.dto.auth.request.AuthRequestDto;
import com.chatty.repository.token.RefreshTokenRepository;
import com.chatty.utils.JwtTokenUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TYPE = "Bearer ";
    private static final String MOBILE_NUMBER = "mobileNumber";
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

    public String getMobileNumber(String accessToken) {
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload()
                .get(MOBILE_NUMBER, String.class);
    }

    public String getUuidByRefreshToken(String refreshToken){
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(refreshToken).getPayload().get(UUID,String.class);
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

    public String createRefreshToken(String mobileNumber, String uuid) {

        Claims claims = Jwts.claims()
                .add(UUID, JwtTokenUtils.getRefreshTokenUuid(mobileNumber,uuid))
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED_TIME))
                .signWith(key)
                .compact();
    }

    public boolean isExistToken(String token) {
        if (token == null || token.isBlank()) {
            log.error("token이 존재 하지 않습니다.");
            return false;
        }

        return true;
    }

    public boolean isRightFormat(String token) {

        if (!token.startsWith(BEARER_TYPE)) {
            log.error("형식에 맞지 않은 token 발송");
            return false;
        }
        return true;
    }

    public boolean isValidToken(String token){
        log.info("[JwtTokenProvider/isValidToken] 비밀키를 통해 유효한 토큰인지 확인");
        log.info(token);
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            return true;
        }catch(ExpiredJwtException e){
            return true;
        }catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    public boolean isExpiredToken(String token) {
        log.info("[JwtTokenProvider/isExpiredToken] 만료된 도큰인지 확인");

        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            log.info("만료되지 않았습니다.");
            return false;
        }catch(ExpiredJwtException e){
            log.info("만료되었습니다.");
            return true;
        }
    }

    public boolean isEqualRedisRefresh(String token, String uuid){

        System.out.println(token);
        System.out.println(uuid);
        log.info("[isEqualRedisRefresh] refreshToken과 Redis에 저장된 refreshToken과 일치여부 확인");
        try {
            String refreshToken = refreshTokenRepository.findRefreshTokenByUuid(uuid);
            System.out.println(refreshToken);
            log.info("[isEqualRedisRefresh] 전달받은 refreshToken이 현재 refreshToken과 일치합니다.");
            return token.equals(refreshToken);
        }catch(Exception e){
            return false;
        }
    }

    public String resolveAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader(AUTHORIZATION);

        return accessToken;
    }
}
