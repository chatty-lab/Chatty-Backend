package com.chatty.controller.chat;

import com.chatty.dto.DataResponseDto;
import com.chatty.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/room")
    public DataResponseDto<String> createRoom(){
        chatService.createRoom();
        return DataResponseDto.of("채팅방이 생성되었습니다.");
    }


}
