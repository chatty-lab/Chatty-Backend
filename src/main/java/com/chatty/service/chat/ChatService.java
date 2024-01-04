package com.chatty.service.chat;

import com.chatty.constants.Code;
import com.chatty.dto.chat.request.MessageDto;
import com.chatty.dto.chat.request.UnreadMessageDto;
import com.chatty.entity.chat.ChatMessage;
import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.chat.ChatRoomRepository;
import com.chatty.repository.chat.MessageRepository;
import com.chatty.repository.user.UserRepository;
import com.chatty.service.user.UserService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    private final RoomService roomService;
    private final UserService userService;

    public void saveMessage(Long roomId, MessageDto messageDto){

        Long senderId = messageDto.getSenderId();

        isExistedRoomByRoomId(roomId);
        isExistedSender(senderId);

        User sender = User.builder().id(messageDto.getSenderId()).build();
        User receiver = User.builder().id(messageDto.getReceiverId()).build();
        ChatRoom chatRoom = roomService.findChatRoom(roomId);

        messageRepository.save(ChatMessage.to(chatRoom,sender,receiver,messageDto.getContent()));

        log.info("메시지 저장이 완료되었습니다.");
    }

    @Transactional
    public void markMessageAsRead(Long messageId){
        ChatMessage message = messageRepository.findChatMessagesByMessageId(messageId).orElseThrow(() -> new CustomException(Code.NOT_FOUND_CHAT_MESSAGE));
        message.setIsRead();
    }

    public List<MessageDto> getMessages(UnreadMessageDto unreadMessageDto){ // 읽지 않은 메세지 위주로

        log.info("{}가 읽지 않은 메시지 전송", unreadMessageDto.getReceiverId());

        User receiver = userService.validateExistUser(unreadMessageDto.getReceiverId());
        User sender = userService.validateExistUser(unreadMessageDto.getSenderId());
        List<ChatMessage> messages = messageRepository.findByIsReadFalseAndReceiverAndSenderOrderBySendTimeDesc(receiver,sender).orElseThrow(() -> new CustomException(Code.NOT_FOUND_CHAT_MESSAGE));

        return messages.stream().map(message -> MessageDto.to(message)).collect(Collectors.toList());
    }

    private void isExistedRoomByRoomId(Long roomId){
        chatRoomRepository.findChatRoomByRoomId(roomId).orElseThrow(() -> new CustomException(Code.NOT_FOUND_CHAT_ROOM));
    }

    private void isExistedSender(Long senderId){
        userRepository.findUserById(senderId).orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));
    }
}
