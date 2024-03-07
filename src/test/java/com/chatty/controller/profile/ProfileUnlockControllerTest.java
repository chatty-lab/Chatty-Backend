package com.chatty.controller.profile;

import com.chatty.dto.profileUnlock.request.ProfileUnlockRequest;
import com.chatty.service.profileUnlock.ProfileUnlockService;
import com.chatty.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileUnlockController.class)
class ProfileUnlockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProfileUnlockService profileUnlockService;

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("01012345678", "", List.of()));
    }

    @DisplayName("상대방 프로필 잠금을 해제한다.")
    @Test
    void unlockProfile() throws Exception {
        // given
        ProfileUnlockRequest request = ProfileUnlockRequest.builder()
                .unlockMethod("candy")
                .build();

        // when // then
        mockMvc.perform(
                        post("/v1/users/profile/{userId}", 2L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("상대방 프로필 잠금을 해제할 때, candy or ticket을 입력해야 합니다.")
    @Test
    void unlockProfileWithFalse() throws Exception {
        // given
        ProfileUnlockRequest request = ProfileUnlockRequest.builder()
//                .unlock(false)
                .build();

        // when // then
        mockMvc.perform(
                        post("/v1/users/profile/{userId}", 2L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("candy또는 ticket을 입력해주세요."));
    }
}