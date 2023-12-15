package com.chatty.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class RefreshTokenRepository {
    private RedisTemplate<String, String> redisTemplateToken;

    @Autowired
    public RefreshTokenRepository(@Qualifier("redisTemplate") RedisTemplate<String, String> redisTemplateToken){
        this.redisTemplateToken = redisTemplateToken;
    }

    public void save(String uuid, String refreshToken){
        try {
            ValueOperations<String, String> value = redisTemplateToken.opsForValue();
            value.set(uuid,refreshToken);
        }catch(Exception e) {
            log.error("[RedistTokenService/getRefreshTokenByUuid] 데이터 저장 실패");
        }
    }

    public String findRefreshTokenByUuid(String uuid){
        try {
            ValueOperations<String, String> value = redisTemplateToken.opsForValue();
            return value.get(uuid);
        }catch(Exception e) {
            log.error("[RedistTokenService/getRefreshTokenByUuid] 일치하는 refresh 토큰이 존재하지 않습니다.");
            return null;
        }
    }

    public void delete(String uuid){
        redisTemplateToken.delete(uuid);
    }
}
