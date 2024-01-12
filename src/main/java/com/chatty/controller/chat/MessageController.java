package com.chatty.controller.chat;
import com.chatty.dto.ApiResponse;
import com.chatty.dto.chat.request.MessageDto;
import com.chatty.dto.chat.request.UnreadMessageDto;
import com.chatty.dto.chat.response.MessageMarkResponseDto;
import com.chatty.dto.chat.response.MultipleMessageResponseDto;
import com.chatty.dto.chat.response.SimpleMessageResponseDto;
import com.chatty.service.chat.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입 실패",
            content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "E-004", value = """
                                    {
                                        "errorCode": "004",
                                        "status": "400",
                                        "message": "존재하지 않는 유저 입니다."
                                    }
                                    """),
                            @ExampleObject(name = "E-007", value = """
                                    {
                                        "errorCode": "007",
                                        "status": "400",
                                        "message": "accessToken 유효성 검증을 실패했습니다."
                                    }
                                    """),
                            @ExampleObject(name = "E-011", value = """
                                    {
                                        "errorCode": "011",
                                        "status": "400",
                                        "message": "채팅방이 존재하지 않습니다."
                                    }
                                    """)
                    }
            )
    )
    @MessageMapping(value = "/chat/message/{roomId}")
    @SendTo(value = "/sub/chat/{roomId}")
    public ApiResponse<SimpleMessageResponseDto> message(@DestinationVariable Long roomId, MessageDto messageDto){
        log.info("메세지 전송");
        return ApiResponse.ok(chatService.saveMessage(roomId, messageDto));
    }

    @Operation(summary = "메시지 읽음처리", description = "메시지를 읽음 처리합니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입 실패",
            content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "E-007", value = """
                                    {
                                        "errorCode": "007",
                                        "status": "400",
                                        "message": "accessToken 유효성 검증을 실패했습니다."
                                    }
                                    """),
                            @ExampleObject(name = "E-013", value = """
                                    {
                                        "errorCode": "013",
                                        "status": "400",
                                        "message": "채팅내용이 존재하지 않습니다."
                                    }
                                    """),
                    }
            )
    )
    @MessageMapping(value = "/readMessage/{messageId}")
    public ApiResponse<MessageMarkResponseDto> readMessage(@DestinationVariable Long messageId){
        log.info("메시지 읽음 처리 - 아이디 : {}",messageId);
        return ApiResponse.ok(chatService.markMessageAsRead(messageId));
    }

    @Operation(summary = "메시지 가져오기", description = "읽지 않은 메시지를 가져옵니다.")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "회원가입 실패",
            content = @Content(mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "E-007", value = """
                                    {
                                        "errorCode": "007",
                                        "status": "400",
                                        "message": "accessToken 유효성 검증을 실패했습니다."
                                    }
                                    """),
                            @ExampleObject(name = "E-004", value = """
                                    {
                                        "errorCode": "004",
                                        "status": "400",
                                        "message": "존재하지 않는 유저 입니다."
                                    }
                                    """),
                            @ExampleObject(name = "E-011", value = """
                                    {
                                        "errorCode": "011",
                                        "status": "400",
                                        "message": "채팅방이 존재하지 않습니다."
                                    }
                                    """),
                    }
            )
    )
    @PostMapping("/chat/messages")
    public ApiResponse<MultipleMessageResponseDto> getMessages(@RequestBody UnreadMessageDto unreadMessageDto){
        return ApiResponse.ok(chatService.getMessages(unreadMessageDto));
    }

}
