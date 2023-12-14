package com.chatty.entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@RedisHash(value = "refresh_token", timeToLive = 1000 * 60 * 3)
public class RefreshToken {

    @Id
    String id;

    @Indexed
    private String uuid;

    private String refreshToken;
}
