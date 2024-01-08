package com.chatty.service.chat;

import com.chatty.constants.Code;
import com.chatty.dto.chat.request.RoomDto;
import com.chatty.dto.chat.response.RoomResponseDto;
import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.chat.ChatRoomRepository;
import com.chatty.service.user.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    @Transactional
    public RoomResponseDto createRoom(RoomDto roomDto){
        User receiver = userService.validateExistUser(roomDto.getReceiverId());
        User sender = userService.validateExistUser(roomDto.getSenderId());

        isExistedRoomByUserId(sender,receiver);

        ChatRoom chatRoom = ChatRoom.builder().sender(sender).receiver(receiver).build();
        log.info("채팅방을 생성했습니다.");

        return RoomResponseDto.of(chatRoomRepository.save(chatRoom));
    }

    public String deleteRoom(Long roomId) {
        isExistedRoomByRoomId(roomId);
        log.info("채팅방을 삭제했습니다.");
        if(roomId == 1){
            System.out.println("throw 탑니다.");
            throw new CustomException(Code.NOT_FOUND_CHAT_ROOM);
        }
        log.info("조건문 빠져나왓다.");

        return "채팅방 삭제 완료";
    }

    @Transactional
    public RoomResponseDto findChatRoom(long roomId){
        return RoomResponseDto.of(chatRoomRepository.findById(roomId).orElseThrow(() -> new CustomException(Code.NOT_FOUND_CHAT_ROOM)));
    }

    private void isExistedRoomByUserId(User sender, User receiver){
        Optional<ChatRoom> optionalChatRoom1 = chatRoomRepository.findChatRoomBySenderAndReceiver(sender,receiver);
        Optional<ChatRoom> optionalChatRoom2 = chatRoomRepository.findChatRoomByReceiverAndSender(receiver,sender);

        if(optionalChatRoom1.isPresent() || optionalChatRoom2.isPresent()){
            throw new CustomException(Code.ALREADY_EXIST_CHATROOM);
        }
    }

    private void isExistedRoomByRoomId(Long roomId){
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findChatRoomByRoomId(roomId);
        if(!optionalChatRoom.isPresent()){
            throw new CustomException(Code.NOT_FOUND_CHAT_ROOM);
        }
    }
}
