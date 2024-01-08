package com.chatty.controller.chat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import com.chatty.service.chat.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import com.chatty.dto.chat.request.RoomDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@WebMvcTest(controllers = RoomController.class)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RoomService roomService;


    @DisplayName("채팅방을 생성한다.")
    @Test
    @WithMockUser(username = "123123", roles = "USER")
    void createChatRoom() throws Exception {
        //given
        RoomDto roomDto = RoomDto.builder()
                .senderId(1L)
                .receiverId(2L)
                .build();

        // when
        User sender = User.builder().id(1L).build();
        User receiver = User.builder().id(2L).build();
        ChatRoom chatRoom = ChatRoom.builder().sender(sender).receiver(receiver).build();
        when(roomService.createRoom(any(RoomDto.class))).thenReturn(chatRoom);

        // then
        mockMvc.perform(
                post("/chat/create/room").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomDto))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("채팅방을 삭제한다.")
    @WithMockUser(username = "123123", roles = "USER")
    void removeChatRoom() throws Exception{
        //given
        Long roomId = 1L;

        //when
        when(roomService.deleteRoom(anyLong())).thenReturn("채팅방이 삭제되었습니다.");

        //then
        mockMvc.perform(
                get("/chat/delete/room/{roomId}",roomId).with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }

    @Test
    @DisplayName("채팅방을 찾는다.")
    @WithMockUser(username = "123123", roles = "USER")
    void getRoom() throws Exception{
        //given
        Long roomId = 1L;

        User sender = User.builder().id(1L).build();
        User receiver = User.builder().id(2L).build();
        ChatRoom chatRoom = ChatRoom.builder().sender(sender).receiver(receiver).build();

        //when
        when(roomService.findChatRoom(anyLong())).thenReturn(chatRoom);

        //then
        mockMvc.perform(
                get("/chat/room/{roomId}",roomId).with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").exists());
    }
}
