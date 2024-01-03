package com.chatty.service.chat;

import com.chatty.constants.Code;
import com.chatty.dto.chat.request.MessageDto;
import com.chatty.entity.chat.ChatMessage;
import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.chat.ChatRoomRepository;
import com.chatty.repository.chat.MessageRepository;
import com.chatty.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final RoomService roomService;

    public void saveMessage(Long roomId, MessageDto messageDto){

        Long senderId = messageDto.getSenderId();

        isExistedRoomByRoomId(roomId);
        isExistedSender(senderId);

        User sender = User.builder().id(messageDto.getSenderId()).build();
        ChatRoom chatRoom = roomService.findChatRoom(roomId);

        messageRepository.save(ChatMessage.to(chatRoom,sender,messageDto.getContent()));

        log.info("메시지 저장이 완료되었습니다.");
    }

    private void isExistedRoomByRoomId(Long roomId){
        chatRoomRepository.findChatRoomByRoomId(roomId).orElseThrow(() -> new CustomException(Code.NOT_FOUND_CHAT_ROOM));
    }

    private void isExistedSender(Long senderId){
        userRepository.findUserById(senderId).orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));
    }
}
