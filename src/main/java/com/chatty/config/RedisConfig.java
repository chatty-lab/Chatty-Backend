package com.chatty.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisConfig {

    @Value("${redis-token-host}")
    private String tokenHost;

    @Value("${redis-authenticationNumber-host}")
    private String authNumberHost;

    @Value("${redis-chat-host}")
    private String chatHost;

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
    public RedisConnectionFactory redisConnectionFactoryChat(){
        return new LettuceConnectionFactory(chatHost,port);
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

    @Bean
    public RedisTemplate<String, Object> redisTemplateChat(){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactoryAuthenticationNumber());

        return redisTemplate;
    }

    // redis pub/sub 메시지처리 listener 설정
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(@Qualifier("redisConnectionFactoryChat") RedisConnectionFactory connectionFactory){
      RedisMessageListenerContainer container = new RedisMessageListenerContainer();
      container.setConnectionFactory(connectionFactory);
      return container;
    }

}
