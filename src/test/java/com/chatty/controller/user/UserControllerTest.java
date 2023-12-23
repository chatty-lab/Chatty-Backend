package com.chatty.controller.user;

import com.chatty.dto.user.request.UserBirthRequest;
import com.chatty.dto.user.request.UserGenderRequest;
import com.chatty.dto.user.request.UserMbtiRequest;
import com.chatty.dto.user.request.UserNicknameRequest;
import com.chatty.entity.user.Authority;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.Mbti;
import com.chatty.service.user.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("01012345678", "", List.of()));
    }

    @DisplayName("회원가입시 닉네임을 등록한다.")
    @Test
    void updateNickname() throws Exception {
        // given
        UserNicknameRequest request = UserNicknameRequest.builder()
                .nickname("닉넴")
                .build();

        // when // then
        mockMvc.perform(
                        put("/users/nickname").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("회원가입시 닉네임을 등록할 때, 닉네임은 필수 값이다.")
    @Test
    void updateNicknameWithoutNickname() throws Exception {
        // given
        UserNicknameRequest request = UserNicknameRequest.builder()
//                .nickname("닉네임")
                .build();

        // when // then
        mockMvc.perform(
                        put("/users/nickname").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("닉네임은 필수로 입력해야 합니다."));
    }

    @DisplayName("회원가입시 닉네임을 등록할 때, 닉네임의 길이는 2~10자로 입력해야 한다.")
    @CsvSource({"닉", "닉네임11글자입니다요", "띄 어 쓰 기"})
    @ParameterizedTest
    void updateNicknameWithNicknameSize(final String nickname) throws Exception {
        // given
        UserNicknameRequest request = UserNicknameRequest.builder()
                .nickname(nickname)
                .build();

        // when // then
        mockMvc.perform(
                        put("/users/nickname").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("닉네임은 특수문자를 제외한 2~10자리여야 합니다."));
    }
    
    @DisplayName("성별을 등록한다.")
    @Test
    void updateGender() throws Exception {
        // given
        UserGenderRequest request = UserGenderRequest.builder()
                .gender(Gender.MALE)
                .build();

        // when // then
        mockMvc.perform(
                        put("/users/gender").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("성별을 등록할 때, 성별을 꼭 입력해야 된다.")
    @Test
    void updateGenderWithoutGender() throws Exception {
        // given
        UserGenderRequest request = UserGenderRequest.builder()
//                .gender(Gender.MALE)
                .build();

        // when // then
        mockMvc.perform(
                        put("/users/gender").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("성별은 필수로 선택해야 됩니다."));
    }

    @DisplayName("생년월일을 등록한다.")
    @Test
    void updateBirth() throws Exception {
        // given
        UserBirthRequest request = UserBirthRequest.builder()
                .birth(LocalDate.now())
                .build();

        // when // then
        mockMvc.perform(
                put("/users/birth").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("생년월일을 등록할 때, 생년월일은 필수로 입력해야 된다.")
    @Test
    void updateBirthWithoutBirth() throws Exception {
        // given
        UserBirthRequest request = UserBirthRequest.builder()
//                .birth(LocalDate.now())
                .build();

        // when // then
        mockMvc.perform(
                put("/users/birth").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("생년월일은 필수로 입력해야 됩니다."));
    }

    @DisplayName("MBTI를 등록한다.")
    @Test
    void updateMbti() throws Exception {
        // given
        UserMbtiRequest request = UserMbtiRequest.builder()
                .mbti(Mbti.ESFJ)
                .build();

        // when // then
        mockMvc.perform(
                        put("/users/mbti").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("MBTI를 등록할 때, MBTI 값을 필수로 입력해야 한다.")
    @Test
    void updateMbtiWithoutMbti() throws Exception {
        // given
        UserMbtiRequest request = UserMbtiRequest.builder()
//                .mbti(Mbti.ESFJ)
                .build();

        // when // then
        mockMvc.perform(
                        put("/users/mbti").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("MBTI는 필수로 입력해야 됩니다."));
    }
}