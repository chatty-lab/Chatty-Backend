package com.chatty.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    private static final long accessTokenExpirationTime = 1000 * 30;
    private final SecretKey key;

    public JwtTokenProvider(@Value("${jwt-secret-key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String getMobileNumber(String token){
        token = token.split(" ")[1];
        return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload().get("mobileNumber",String.class);
    }

    public String createToken(String mobileNumber, String uuid){

        Claims claims = Jwts.claims()
                .add("mobileNumber", mobileNumber)
                .add("uuid", uuid)
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpirationTime))
                .signWith(key)
                .compact();
    }

    public boolean isExistToken(final String token){
        if(token == null){
            log.error("token이 존재하지 않습니다.");
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
