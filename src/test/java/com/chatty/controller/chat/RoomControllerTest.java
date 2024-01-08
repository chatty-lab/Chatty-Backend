package com.chatty.controller.chat;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import com.chatty.dto.chat.request.RoomDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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


    @DisplayName("채팅방을 생성한다.")
    @Test
    @WithMockUser(username = "123123", roles = "USER")
    void createChatRoom() throws Exception {
        //given
        RoomDto roomDto = RoomDto.builder()
                .senderId(1L)
                .receiverId(2L)
                .build();

        //when, then
        mockMvc.perform(
                post("/chat/room").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roomDto))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("채팅방 생성시, senderId 필수 값이다.")
    @WithMockUser(username = "123123", roles = "USER")
    void createChatRoomWithoutSenderId() throws Exception{
        //given
        RoomDto roomDto = RoomDto.builder()
                .receiverId(1L)
                .build();
        //when, then
        mockMvc.perform(
                        post("/chat/room").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roomDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("senderId(송신자)는 필수로 입력해야 합니다."));
    }

    @Test
    @DisplayName("채팅방 생성시, receiverId는 필수 값이다.")
    @WithMockUser(username = "123123", roles = "USER")
    void createChatRoomWithoutReceiverId() throws Exception{
        //given
        RoomDto roomDto = RoomDto.builder()
                .senderId(1L)
                .build();
        //when, then
        mockMvc.perform(
                        post("/chat/room").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(roomDto))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("receiverId(수신자)는 필수로 입력해야 합니다."));
    }

    @Test
    @DisplayName("채팅방을 삭제한다.")
    @WithMockUser(username = "123123", roles = "USER")
    void removeChatRoom() throws Exception{
        //given
        Long roomId = 1L;

        //then
        mockMvc.perform(
                delete("/chat/room/{roomId}",roomId).with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("채팅방을 찾는다.")
    @WithMockUser(username = "123123", roles = "USER")
    void getRoom() throws Exception{
        //given
        Long roomId = 1L;

        //then
        mockMvc.perform(
                get("/chat/room/{roomId}",roomId).with(csrf())
        )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
