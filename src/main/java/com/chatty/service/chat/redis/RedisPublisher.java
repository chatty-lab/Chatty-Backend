package com.chatty.service.chat.redis;

import com.chatty.entity.chat.ChatMessage;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    @Resource(name = "redisTemplateChat")
    private final RedisTemplate<String,Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessage chatMessage){
        redisTemplate.convertAndSend(topic.getTopic(), chatMessage);
    }
}
