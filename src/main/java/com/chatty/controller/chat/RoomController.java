package com.chatty.controller.chat;

import com.chatty.dto.DataResponseDto;
import com.chatty.dto.chat.request.ChatDto;
import com.chatty.dto.chat.response.RoomResponseDto;
import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import com.chatty.service.chat.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("chat")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/create/room")
    public DataResponseDto<RoomResponseDto> createRoom(@Valid @RequestBody ChatDto chatDto, @AuthenticationPrincipal User userDetails){

        log.info("채팅방 생성");
        Long roomId = roomService.createRoom(chatDto.getUserId(), userDetails);

        return DataResponseDto.of(RoomResponseDto.builder().roomId(roomId).build());
    }

    @PostMapping("/room")
    public DataResponseDto<RoomResponseDto> getRoom(@RequestBody long roomId){
        ChatRoom chatRoom = roomService.findChatRoom(roomId);

        return DataResponseDto.of(RoomResponseDto.builder().roomId(chatRoom.getRoomId()).receiverId(chatRoom.getReceiver().getId()).senderId(chatRoom.getSender().getId()).build());
    }
}
