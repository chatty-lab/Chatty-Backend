package com.chatty.service.chat;

import com.chatty.dto.chat.request.MessageDto;
import com.chatty.repository.chat.ChatRoomRepository;
import com.chatty.repository.chat.MessageRepository;
import com.chatty.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final UserService userService;
    private final RoomService roomService;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    private static final String MESSAGE_CACHE_KEY = "messageCacheRoom:";

    public void saveMessage(MessageDto messageDto, Long roomId){

    }


}
