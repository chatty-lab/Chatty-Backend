package com.chatty.controller.chat;
import com.chatty.dto.ApiResponse;
import com.chatty.dto.chat.request.MessageDto;
import com.chatty.service.chat.ChatService;
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

    private final ChatService chatService;

    @MessageMapping(value = "/chat/message/{roomId}")
    @SendTo(value = "/sub/chat/{roomId}")
    public ApiResponse<MessageDto> message(@DestinationVariable Long roomId, MessageDto messageDto){
        log.info("메세지 전송");
        log.info("{}",messageDto);

        chatService.saveMessage(roomId, messageDto);

        return ApiResponse.ok(MessageDto.builder()
                .roomId(messageDto.getRoomId())
                .senderId(messageDto.getSenderId())
                .content(messageDto.getContent())
                .build());
    }
}
