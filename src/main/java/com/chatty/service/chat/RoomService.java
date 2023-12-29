package com.chatty.service.chat;

import com.chatty.constants.Code;
import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.chat.ChatRoomRepository;
import com.chatty.service.user.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    public Long createRoom(Long receiverId, User userDetails){
        User receiver = userService.validateExistUser(receiverId);
        User sender = userService.validateExistUser(userDetails.getId());

        log.info("채팅방 존재 여부 확인");
        // 채팅방 존재 확인
        Optional<ChatRoom> findChatRoom = chatRoomRepository.findChatRoomBySenderAndReceiver(sender, receiver);

        ChatRoom chatRoom = null;

        if(findChatRoom.isPresent()){
            log.info("채팅방이 존재하는 경우");
            chatRoom = findChatRoom.get();
            log.info("채팅방이 존재합니다.");
            return chatRoom.getRoomId();
        }

        chatRoom = ChatRoom.builder().sender(sender).receiver(receiver).build();
        log.info("채팅방을 생성했습니다.");

        chatRoomRepository.save(chatRoom);

        return chatRoom.getRoomId();
    }

    public ChatRoom findChatRoom(long roomId){
        return chatRoomRepository.findById(roomId).orElseThrow(() -> new CustomException(Code.NOT_FOUND_CHAT_ROOM));
    }
}
