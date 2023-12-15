package com.chatty.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${redis-host}")
    private String host;

    @Value("${redis-token-port}")
    private int tokenPort;

    @Value("${redis-authenticationNumber-port}")
    private int authenticationNumberPort;

    @Bean
    public RedisConnectionFactory redisConnectionFactoryToken(){
        return new LettuceConnectionFactory(host, tokenPort);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactoryAuthenticationNumber(){
        return new LettuceConnectionFactory(host,authenticationNumberPort);
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(){
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactoryToken());

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplateAuthNumber(){
        RedisTemplate<String,String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactoryAuthenticationNumber());

        return redisTemplate;
    }
}
