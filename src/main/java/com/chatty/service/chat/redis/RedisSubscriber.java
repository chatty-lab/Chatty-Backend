package com.chatty.service.chat.redis;

import com.chatty.entity.chat.PublishMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedisSubscriber{
    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    public void sendMessage(String message, byte[] pattern) {
        try {
            log.info("publish 전 message : {}", message);
            PublishMessage publishMessage = objectMapper.readValue(message, PublishMessage.class);
            messagingTemplate.convertAndSend("/sub/chats/" + publishMessage.getRoomId(), publishMessage);
            log.info("publish 전 message : {}", publishMessage.getContent());

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
