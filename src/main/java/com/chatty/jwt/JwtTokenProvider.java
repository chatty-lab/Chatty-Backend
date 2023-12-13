package com.chatty.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt-secret-key}")
    private String secretKey;

    @Value("${jwt-access-token-expiration-time}")
    private long ACCESS_TOKEN_EXPIRED_TIME;

    @Value("${jwt-refresh-token-expiration-time}")
    private long REFRESH_TOKEN_EXPIRED_TIME;

    private SecretKey key;

    @PostConstruct
    public void init(){
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String getMobileNumber(String token){
        token = token.split(" ")[1];
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("mobileNumber",String.class);
    }

    public Map<String,String> createTokens(String mobileNumber, String uuid){

        Claims claims = Jwts.claims()
                .add("mobileNumber", mobileNumber)
                .add("uuid", uuid)
                .build();

        String accessToken = createAccessToken(claims);
        String refreshToken = createRefreshToken();

        Map<String,String> tokens = new HashMap<>();
        tokens.put("accessToken",accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    private String createAccessToken(Claims claims){
        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRED_TIME))
                .signWith(key)
                .compact();
    }

    private String createRefreshToken(){
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRED_TIME))
                .signWith(key)
                .compact();
    }

    public boolean isExistToken(final String token){
        if(token == null){
            log.error("token이 존재 하지 않습니다.");
            return false;
        }

        return true;
    }

    public boolean isRightFormat(final String token, final String BEARER_TYPE){

        log.info("BEARER_TYPE : {}",BEARER_TYPE);
        log.info("startsWith : {}", token.startsWith(BEARER_TYPE));
        if(!token.startsWith(BEARER_TYPE)){
            log.error("형식에 맞지 않은 token 발송");
            return false;
        }
        return true;
    }

    public boolean isExpiredToken(String token){
        token = token.split(" ")[1];
        log.info("token : {}", token);
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().getExpiration().
                before(new Date());
    }
}
