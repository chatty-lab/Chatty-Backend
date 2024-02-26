package com.chatty.controller.subscription;

import com.chatty.config.SecurityConfig;
import com.chatty.dto.subscription.request.SubscriptionCreateRequest;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.service.subscription.SubscriptionService;
import com.chatty.service.user.UserDetailsServiceImpl;
import com.chatty.service.user.UserService;
import com.chatty.validator.TokenValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminSubscriptionController.class)
@EnableMethodSecurity
class AdminSubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SubscriptionService subscriptionService;

    @DisplayName("ADMIN 권한이 새로운 구독권을 등록한다.")
    @WithMockUser(username = "01012345678", roles = "ADMIN")
    @Test
    void createSubscription() throws Exception {
        // given
        SubscriptionCreateRequest request = SubscriptionCreateRequest.builder()
                .name("구독권1")
                .duration(30)
                .price(9_900)
                .build();

        // when // then
        mockMvc.perform(
                        post("/v1/subscription").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("ADMIN 권한이 아닌 사람이 새로운 구독권을 등록하면 403예외가 발생한다.")
    @WithMockUser(username = "01012345678", roles = "USER")
    @Test
    void createSubscriptionWithoutAdmin() throws Exception {
        // given
        SubscriptionCreateRequest request = SubscriptionCreateRequest.builder()
                .name("구독권1")
                .duration(30)
                .price(9_900)
                .build();

        // when // then
        mockMvc.perform(
                        post("/v1/subscription").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}