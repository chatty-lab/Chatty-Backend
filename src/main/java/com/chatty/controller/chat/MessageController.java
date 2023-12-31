package com.chatty.controller.chat;
import com.chatty.dto.ApiResponse;
import com.chatty.dto.chat.request.MessageDto;
import com.chatty.dto.chat.request.UnreadMessageDto;
import com.chatty.dto.chat.response.MessageMarkResponseDto;
import com.chatty.dto.chat.response.MultipleMessageResponseDto;
import com.chatty.dto.chat.response.SimpleMessageResponseDto;
import com.chatty.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final ChatService chatService;

    @Operation(summary = "메시지 전송", description = "구독자들에게 메시지를 전송합니다. 경로 맨앞에 pub을 붙여주어야 합니다. ex) /pub/chat/message/{roomId}")
    @MessageMapping(value = "/chat/message/{roomId}")
    @SendTo(value = "/sub/chat/{roomId}")
    public ApiResponse<SimpleMessageResponseDto> message(@DestinationVariable Long roomId, MessageDto messageDto){
        log.info("메세지 전송");
        return ApiResponse.ok(chatService.saveMessage(roomId, messageDto));
    }

    @Operation(summary = "메시지 읽음처리", description = "메시지를 읽음 처리합니다.")
    @MessageMapping(value = "/readMessage/{messageId}")
    public ApiResponse<MessageMarkResponseDto> readMessage(@DestinationVariable Long messageId){
        log.info("메시지 읽음 처리 - 아이디 : {}",messageId);
        return ApiResponse.ok(chatService.markMessageAsRead(messageId));
    }

    @Operation(summary = "메시지 가져오기", description = "읽지 않은 메시지를 가져옵니다.")
    @PostMapping("/chat/messages")
    public ApiResponse<MultipleMessageResponseDto> getMessages(@RequestBody UnreadMessageDto unreadMessageDto){
        return ApiResponse.ok(chatService.getMessages(unreadMessageDto));
    }

}
