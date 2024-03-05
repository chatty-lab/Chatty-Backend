package com.chatty.controller.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chatty.dto.auth.request.CheckTokenDto;
import com.chatty.service.auth.AuthService;
import com.chatty.service.sms.SmsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private SmsService smsService;

//    @Test
//    @DisplayName("accessToken 유효성을 검증한다.")
//    @WithMockUser(username = "123123", roles = "USER")
//    void token() throws Exception{
//        //given
//        CheckTokenDto checkTokenDto = CheckTokenDto.builder()
//                .accessToken("abcdefg").build();
//        //when, then
//        mockMvc.perform(
//                post("/auth/token").with(csrf())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(checkTokenDto))
//                )
//                .andDo(print())
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("accessToken 유효성을 검증시, accessToken 값은 필수이다.")
//    @WithMockUser(username = "123123", roles = "USER")
//    void tokenNotExistedToken() throws Exception{
//        //given
//        CheckTokenDto checkTokenDto = CheckTokenDto.builder().build();
//        //when, then
//        mockMvc.perform(
//                        post("/auth/token").with(csrf())
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(objectMapper.writeValueAsString(checkTokenDto))
//                )
//                .andDo(print())
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("accessToken은 필수로 입력해야 합니다."));
//    }
}
