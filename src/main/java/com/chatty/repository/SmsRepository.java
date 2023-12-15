package com.chatty.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class SmsRepository {
    private RedisTemplate<String, String> redisTemplateAuthNumber;

    @Autowired
    public SmsRepository(@Qualifier("redisTemplateAuthNumber") RedisTemplate<String, String> redisTemplateAuthenticationNumber){
        this.redisTemplateAuthNumber = redisTemplateAuthenticationNumber;
    }

    public void save(String mobileNumber, String authNumber) {
        try {
            ValueOperations<String, String> value = redisTemplateAuthNumber.opsForValue();
            value.set(mobileNumber,authNumber);
        }catch(Exception e) {
            log.error("[RedistTokenService/getRefreshTokenByUuid] 데이터 저장 실패");
        }
    }

    public String findAuthNumberByMobileNumber(String mobileNumber) {
        try {
            ValueOperations<String, String> value = redisTemplateAuthNumber.opsForValue();
            return value.get(mobileNumber);
        }catch(Exception e) {
            log.error("[RedistTokenService/getRefreshTokenByUuid] 일치하는 refresh 토큰이 존재하지 않습니다.");
            return null;
        }
    }

    public void delete(String mobileNumber) {
        redisTemplateAuthNumber.delete(mobileNumber);
    }
}
