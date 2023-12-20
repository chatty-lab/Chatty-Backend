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

    @Value("${redis-token-host}")
    private String tokenHost;

    @Value("${redis-authenticationNumber-host}")
    private String authNumberHost;

    @Value("${redis-port}")
    private int port;


    @Bean
    public RedisConnectionFactory redisConnectionFactoryToken(){
        return new LettuceConnectionFactory(tokenHost, port);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactoryAuthenticationNumber(){
        return new LettuceConnectionFactory(authNumberHost,port);
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
