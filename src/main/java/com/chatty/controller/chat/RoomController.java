package com.chatty.controller.chat;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.chat.request.RoomDto;
import com.chatty.dto.chat.response.RoomResponseDto;
import com.chatty.service.chat.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/room")
    public ApiResponse<RoomResponseDto> createRoom(@Valid @RequestBody RoomDto roomDto){
        log.info("채팅방 생성");
        return ApiResponse.ok(roomService.createRoom(roomDto));
    }

    @DeleteMapping("/room/{roomId}")
    public ApiResponse<String> deleteRoom(@PathVariable Long roomId){
        log.info("채팅방 삭제");
        return ApiResponse.ok(roomService.deleteRoom(roomId));
    }

    @GetMapping("/room/{roomId}")
    public ApiResponse<RoomResponseDto> getRoom(@PathVariable Long roomId){
        log.info("채팅방 찾기");
        return ApiResponse.ok(roomService.findChatRoom(roomId));
    }
}
