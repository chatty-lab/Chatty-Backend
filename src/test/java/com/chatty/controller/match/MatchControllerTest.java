package com.chatty.controller.match;

import com.chatty.dto.match.request.MatchRequest;
import com.chatty.entity.user.Gender;
import com.chatty.service.match.MatchService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MatchController.class)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MatchService matchService;

    @BeforeEach
    void setUp() {
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken("01012345678", "", List.of()));
    }

    @DisplayName("실시간 매칭을 등록한다.")
    @Test
    void createMatch() throws Exception {
        // given
        MatchRequest request = MatchRequest.builder()
                .minAge(25)
                .maxAge(30)
                .category("category")
                .gender(Gender.FEMALE)
                .build();

        // when // then
        mockMvc.perform(
                post("/match").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.message").value("OK"));
    }

    @DisplayName("실시간 매칭을 등록할 때, 최소 나이는 양수이다.")
    @Test
    void createMatchWithZeroMinAge() throws Exception {
        // given
        MatchRequest request = MatchRequest.builder()
                .minAge(0)
                .maxAge(30)
                .category("category")
                .gender(Gender.FEMALE)
                .build();

        // when // then
        mockMvc.perform(
                post("/match").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("000"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("최소 나이는 양수여야 합니다."));
    }

    @DisplayName("실시간 매칭을 등록할 때, 최대 나이는 양수이다.")
    @Test
    void createMatchWithZeroMaxAge() throws Exception {
        // given
        MatchRequest request = MatchRequest.builder()
                .minAge(25)
                .maxAge(0)
                .category("category")
                .gender(Gender.FEMALE)
                .build();

        // when // then
        mockMvc.perform(
                post("/match").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("000"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("최대 나이는 양수여야 합니다."));
    }

    @DisplayName("실시간 매칭을 등록할 때, 관심사는 필수값이다.")
    @Test
    void createMatchWithoutCategory() throws Exception {
        // given
        MatchRequest request = MatchRequest.builder()
                .minAge(25)
                .maxAge(30)
//                .category("category")
                .gender(Gender.FEMALE)
                .build();

        // when // then
        mockMvc.perform(
                post("/match").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("000"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("관심사는 필수로 선택해야 합니다."));
    }

    @DisplayName("실시간 매칭을 등록할 때, 성별은 필수값이다.")
    @Test
    void createMatchWithoutGender() throws Exception {
        // given
        MatchRequest request = MatchRequest.builder()
                .minAge(25)
                .maxAge(30)
                .category("category")
//                .gender(Gender.FEMALE)
                .build();

        // when // then
        mockMvc.perform(
                post("/match").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("000"))
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("성별은 필수로 선택해야 합니다."));
    }

}