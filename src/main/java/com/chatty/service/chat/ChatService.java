package com.chatty.service.chat;

import com.chatty.entity.chat.ChatRoom;
import com.chatty.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;

    public void createRoom() {
        chatRoomRepository.save(ChatRoom.create());
    }
}
