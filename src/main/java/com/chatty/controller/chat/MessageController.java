package com.chatty.controller.chat;
import com.chatty.dto.chat.request.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    // stompConfig 에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합
    // /pub/chat/message/{roomId} 메세지가 오면 동작
    @MessageMapping(value = "/chat/message/{roomId}")
    @SendTo(value = "/sub/chat/{roomId}")
    public MessageDto message(@DestinationVariable Long roomId, MessageDto messageDto){
        log.debug("메세지 전송 {}", roomId);
        log.info("메세지 전송");
        log.info("{}",messageDto);

        return MessageDto.builder()
                .roomId(roomId)
                .senderId(messageDto.getSenderId())
                .content(messageDto.getContent())
                .build();
    }
}
