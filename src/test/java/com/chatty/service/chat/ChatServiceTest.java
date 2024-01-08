package com.chatty.service.chat;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.chatty.dto.chat.request.MessageDto;
import com.chatty.dto.chat.response.SimpleMessageResponseDto;
import com.chatty.entity.chat.ChatMessage;
import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import com.chatty.repository.chat.ChatRoomRepository;
import com.chatty.repository.chat.MessageRepository;
import com.chatty.repository.user.UserRepository;
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
    @DisplayName("메시지 읽음 처리를 한다.")
    void markMessageAsRead() throws Exception{
        //given

        //when

        //then
    }
}
