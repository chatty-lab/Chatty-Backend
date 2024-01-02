package com.chatty.controller.chat;
import com.chatty.dto.chat.request.MessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessagingTemplate template;

    // stompConfig 에서 설정한 applicationDestinationPrefixes 와 @MessageMapping 경로가 병합
    // /pub/chat/enter에 메세지가 오면 동작
    @MessageMapping("chat/{roomId}")
    @SendTo("/sub/chat/{roomId}")
    public MessageDto enter(@DestinationVariable Long roomId, @Valid MessageDto messageDto){
        log.info("채팅방 입장");
        return MessageDto.builder()
                .roomId(roomId)
                .senderId(messageDto.getSenderId())
                .content(messageDto.getContent() + "님이 채팅방에 입장했습니다.")
                .build();
    }


}
