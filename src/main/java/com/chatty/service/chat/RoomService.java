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

@Service
@Slf4j
@RequiredArgsConstructor
public class RoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserService userService;

    public RoomResponseDto createRoom(RoomDto roomDto){
        User receiver = userService.validateExistUser(roomDto.getReceiverId());
        User sender = userService.validateExistUser(roomDto.getSenderId());

        isExistedRoomByUserId(sender,receiver);

        ChatRoom chatRoom = ChatRoom.builder().sender(sender).receiver(receiver).build();
        log.info("채팅방을 생성했습니다.");

        chatRoomRepository.save(chatRoom);

        return RoomResponseDto.builder().roomId(
                chatRoom.getRoomId()).senderId(sender.getId()).receiverId(receiver.getId()).build();
    }

    public RoomResponseDto deleteRoom(Long roomId) {
        isExistedRoomByRoomId(roomId);
        chatRoomRepository.delete(ChatRoom.builder().roomId(roomId).build());
        log.info("채팅방을 삭제했습니다.");

        return RoomResponseDto.builder().roomId(roomId).build();
    }

    public RoomResponseDto findChatRoom(long roomId){
        ChatRoom chatRoom = chatRoomRepository.findById(roomId).orElseThrow(() -> new CustomException(Code.NOT_FOUND_CHAT_ROOM));

        return RoomResponseDto.builder().roomId(chatRoom.getRoomId()).senderId(chatRoom.getSender().getId()).receiverId(chatRoom.getReceiver().getId()).build();
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
