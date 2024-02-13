package com.chatty.service.chat;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.chatty.constants.Code;
import com.chatty.dto.chat.request.MessageDto;
import com.chatty.dto.chat.request.UnreadMessageDto;
import com.chatty.dto.chat.response.MessageMarkResponseDto;
import com.chatty.dto.chat.response.MultipleMessageResponseDto;
import com.chatty.dto.chat.response.SimpleMessageResponseDto;
import com.chatty.entity.chat.ChatMessage;
import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.chat.ChatRoomRepository;
import com.chatty.repository.chat.MessageRepository;
import com.chatty.repository.user.UserRepository;
import com.chatty.service.user.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {

    @InjectMocks
    private ChatService chatService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChatRoomRepository chatRoomRepository;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("메시지를 저장한다.")
    void saveMessage() throws Exception {
        //given
        Long roomId = 1L;
        MessageDto messageDto = MessageDto.builder()
                .senderId(1L)
                .receiverId(2L)
                .content("메세지 테스트")
                .roomId(1L)
                .build();

        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(1L)
                .sender(User.builder().id(1L).build())
                .receiver(User.builder().id(2L).build())
                .build();

        ChatMessage chatMessage = ChatMessage.builder().messageId(1L).chatRoom(chatRoom)
                .sender(User.builder().id(1L).build()).receiver(User.builder().id(2L).build()).content(messageDto.getContent()).build();

        //when
        when(chatRoomRepository.findChatRoomByRoomId(anyLong())).thenReturn(Optional.of(chatRoom));
        when(messageRepository.save(any(ChatMessage.class))).thenReturn(chatMessage);
        when(userRepository.findUserById(anyLong())).thenReturn(Optional.of(User.builder().id(any()).build()));

        // redisConfig 문제
        SimpleMessageResponseDto simpleMessageResponseDto = chatService.saveMessage(roomId, messageDto);

        //then
        assertThat(simpleMessageResponseDto.getContent()).isEqualTo("메세지 테스트");
        assertThat(simpleMessageResponseDto.getReceiverId()).isEqualTo(2L);
        assertThat(simpleMessageResponseDto.getSenderId()).isEqualTo(1L);
        assertThat(simpleMessageResponseDto.getRoomId()).isEqualTo(1L);

    }

    @Test
    @DisplayName("메세지를 저장할때, 채팅방이 존재하지 않으면 예외가 발생한다.")
    void saveMessageNotExistedRoomId() throws Exception{
        //given
        Long roomId = 1L;
        MessageDto messageDto = MessageDto.builder()
                .senderId(1L)
                .receiverId(2L)
                .content("메세지 테스트")
                .roomId(1L)
                .build();

        //when
        when(chatRoomRepository.findChatRoomByRoomId(anyLong())).thenThrow(new CustomException(Code.NOT_FOUND_CHAT_ROOM));

        // then
        assertThatThrownBy(() -> chatService.saveMessage(roomId,messageDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("채팅방이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("메세지를 저장할때, 유저가 없는 계정이면 예외가 발생한다.")
    void saveMessageNotExistedUser() throws Exception{
        //given
        Long roomId = 1L;
        MessageDto messageDto = MessageDto.builder()
                .senderId(1L)
                .receiverId(2L)
                .content("메세지 테스트")
                .roomId(1L)
                .build();

        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(1L)
                .sender(User.builder().id(1L).build())
                .receiver(User.builder().id(2L).build())
                .build();

        //when
        when(chatRoomRepository.findChatRoomByRoomId(anyLong())).thenReturn(Optional.of(chatRoom));
        when(userRepository.findUserById(anyLong())).thenThrow(new CustomException(Code.NOT_EXIST_USER));

        // then
        assertThatThrownBy(() -> chatService.saveMessage(roomId,messageDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("존재 하지 않는 유저 입니다.");
    }

    @Test
    @DisplayName("메시지 읽음 처리를 한다.")
    void markMessageAsRead() throws Exception{
        //given
        Long messageId = 1L;
        ChatMessage chatMessage = ChatMessage.builder().messageId(messageId).isRead(false).build();

        //when
        when(messageRepository.findChatMessagesByMessageId(messageId)).thenReturn(Optional.of(chatMessage));
        MessageMarkResponseDto messageMarkResponseDto = chatService.markMessageAsRead(messageId);

        //then
        assertThat(messageMarkResponseDto.getMessageId()).isEqualTo(messageId);
        assertThat(messageMarkResponseDto.getIsRead()).isTrue();
    }

    @Test
    @DisplayName("메시지 읽음 처리시, 메세지가 존재하지 않으면 예외가 발생한다.")
    void markMessageAsReadNotExisted() throws Exception{
        //given
        Long messageId = 1L;

        //when
        when(messageRepository.findChatMessagesByMessageId(messageId)).thenThrow(new CustomException(Code.NOT_FOUND_CHAT_MESSAGE));

        //then
        assertThatThrownBy(() -> chatService.markMessageAsRead(messageId))
                .isInstanceOf(CustomException.class)
                .hasMessage("채팅 내용이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("메시지 목록을 가져온다.")
    void getMessages() throws Exception{
        //given
        UnreadMessageDto unreadMessageDto = UnreadMessageDto.builder().roomId(1L).build();
        User sender = User.builder().id(1L).build();
        User receiver = User.builder().id(2L).build();
        ChatRoom chatRoom = ChatRoom.builder().roomId(1L).build();

        ChatMessage chatMessage1 = ChatMessage.builder().messageId(1L).sender(sender).receiver(receiver).chatRoom(chatRoom).build();
        ChatMessage chatMessage2 = ChatMessage.builder().messageId(2L).sender(sender).receiver(receiver).chatRoom(chatRoom).build();

        when(messageRepository.findByIsReadFalseAndChatRoomRoomIdOrderBySendTimeDesc(anyLong())).thenReturn(Optional.of(
                List.of(chatMessage1,chatMessage2)));

        //when
        MultipleMessageResponseDto messages = chatService.getMessages(unreadMessageDto);

        //then
        assertThat(messages.getContents().size()).isEqualTo(2);
        assertThat(messages.getContents().get(0).getMessageId()).isEqualTo(1);
        assertThat(messages.getContents().get(1).getMessageId()).isEqualTo(2);

    }

    @Test
    @DisplayName("메시지 목록을 가져올때, 메시지가 존재하지 않으면 예외가 발생한다.")
    void getMessagesNotFoundMessages() throws Exception{
        //given
        UnreadMessageDto unreadMessageDto = UnreadMessageDto.builder().roomId(1L).build();
        when(messageRepository.findByIsReadFalseAndChatRoomRoomIdOrderBySendTimeDesc(1L)).thenThrow(new CustomException(Code.NOT_FOUND_CHAT_MESSAGE));

        //when, then
        assertThatThrownBy(() -> chatService.getMessages(unreadMessageDto))
                .isInstanceOf(CustomException.class)
                .hasMessage("채팅 내용이 존재하지 않습니다.");
    }
}
