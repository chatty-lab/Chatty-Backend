package com.chatty.repository.token;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository {

    @Value("${jwt-refresh-token-expiration-time}")
    private String validTime;

    private final RedisTemplate<String, String> redisTemplate;

    public void save(String uuid, String refreshToken){
        try {
            ValueOperations<String, String> value = redisTemplate.opsForValue();
            value.set(uuid,refreshToken, Duration.ofSeconds(Long.parseLong(validTime)/1000));
        }catch(Exception e) {
            log.error("[RedistTokenService/getRefreshTokenByUuid] 데이터 저장 실패");
        }
    }

    public String findRefreshTokenByUuid(String uuid){
        try {
            ValueOperations<String, String> value = redisTemplate.opsForValue();
            return value.get(uuid);
        }catch(Exception e) {
            log.error("[RedistTokenService/getRefreshTokenByUuid] 일치하는 refresh 토큰이 존재하지 않습니다.");
            return null;
        }
    }

    public void delete(String uuid){
        redisTemplate.delete(uuid);
    }
}
