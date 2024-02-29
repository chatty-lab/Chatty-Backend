package com.chatty.controller.subscription;

import com.chatty.dto.subscription.request.SubscriptionCreateRequest;
import com.chatty.dto.subscription.request.SubscriptionUpdateRequest;
import com.chatty.service.subscription.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @DisplayName("ADMIN 권한이 새로운 구독권을 등록할 때, 구독권 이름은 필수값이다.")
    @WithMockUser(username = "01012345678", roles = "ADMIN")
    @Test
    void createSubscriptionWithoutName() throws Exception {
        // given
        SubscriptionCreateRequest request = SubscriptionCreateRequest.builder()
//                .name("구독권1")
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
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("E000"))
                .andExpect(jsonPath("$.message").value("구독권 이름은 필수로 입력해야 합니다."));
    }

    @DisplayName("ADMIN 권한이 새로운 구독권을 등록할 때, 구독권 기간은 최소 30일 이상이다.")
    @WithMockUser(username = "01012345678", roles = "ADMIN")
    @Test
    void createSubscriptionWithThirtyDayDuration() throws Exception {
        // given
        SubscriptionCreateRequest request = SubscriptionCreateRequest.builder()
                .name("구독권1")
                .duration(29)
                .price(9_900)
                .build();

        // when // then
        mockMvc.perform(
                        post("/v1/subscription").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("E000"))
                .andExpect(jsonPath("$.message").value("구독권 기간은 30일 이상이여야 합니다."));
    }

    @DisplayName("ADMIN 권한이 새로운 구독권을 등록할 때, 구독권 가격은 양수이다.")
    @WithMockUser(username = "01012345678", roles = "ADMIN")
    @Test
    void createSubscriptionWithZeroPrice() throws Exception {
        // given
        SubscriptionCreateRequest request = SubscriptionCreateRequest.builder()
                .name("구독권1")
                .duration(30)
                .price(0)
                .build();

        // when // then
        mockMvc.perform(
                        post("/v1/subscription").with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("E000"))
                .andExpect(jsonPath("$.message").value("구독권 가격은 양수여야 합니다."));
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

    @DisplayName("등록되어있는 구독권을 수정합니다.")
    @WithMockUser(username = "01012345678", roles = "ADMIN")
    @Test
    void updateSubscription() throws Exception {
        // given
        SubscriptionUpdateRequest request = SubscriptionUpdateRequest.builder()
                .name("수정된 구독권")
                .duration(30)
                .price(9_900)
                .build();

        // when // then
        mockMvc.perform(
                        put("/v1/subscription/{subscriptionId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("구독권을 수정할 때, 구독권 이름은 필수값이다.")
    @WithMockUser(username = "01012345678", roles = "ADMIN")
    @Test
    void updateSubscriptionWithoutName() throws Exception {
        // given
        SubscriptionCreateRequest request = SubscriptionCreateRequest.builder()
//                .name("구독권1")
                .duration(30)
                .price(9_900)
                .build();

        // when // then
        mockMvc.perform(
                        put("/v1/subscription/{subscriptionId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("E000"))
                .andExpect(jsonPath("$.message").value("구독권 이름은 필수로 입력해야 합니다."));
    }

    @DisplayName("ADMIN 권한이 새로운 구독권을 등록할 때, 구독권 기간은 최소 30일 이상이다.")
    @WithMockUser(username = "01012345678", roles = "ADMIN")
    @Test
    void updateSubscriptionWithLessThanThirtyDayDuration() throws Exception {
        // given
        SubscriptionCreateRequest request = SubscriptionCreateRequest.builder()
                .name("구독권1")
                .duration(29)
                .price(9_900)
                .build();

        // when // then
        mockMvc.perform(
                        put("/v1/subscription/{subscriptionId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("E000"))
                .andExpect(jsonPath("$.message").value("구독권 기간은 30일 이상이여야 합니다."));
    }

    @DisplayName("ADMIN 권한이 새로운 구독권을 등록할 때, 구독권 가격은 양수이다.")
    @WithMockUser(username = "01012345678", roles = "ADMIN")
    @Test
    void updateSubscriptionWithZeroPrice() throws Exception {
        // given
        SubscriptionCreateRequest request = SubscriptionCreateRequest.builder()
                .name("구독권1")
                .duration(30)
                .price(0)
                .build();

        // when // then
        mockMvc.perform(
                        put("/v1/subscription/{subscriptionId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("E000"))
                .andExpect(jsonPath("$.message").value("구독권 가격은 양수여야 합니다."));
    }

    @DisplayName("ADMIN 권한이 아닌 사람이 등록되어있는 구독권을 수정할 때, 403예외가 발생한다.")
    @WithMockUser(username = "01012345678", roles = "USER")
    @Test
    void updateSubscriptionWithoutAdmin() throws Exception {
        // given
        SubscriptionUpdateRequest request = SubscriptionUpdateRequest.builder()
                .name("수정된 구독권")
                .duration(30)
                .price(9_900)
                .build();

        // when // then
        mockMvc.perform(
                        put("/v1/subscription/{subscriptionId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @DisplayName("구독권을 삭제한다.")
    @WithMockUser(username = "01012345678", roles = "ADMIN")
    @Test
    void deleteSubscription() throws Exception {
        // when // then
        mockMvc.perform(
                        delete("/v1/subscription/{subscriptionId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("ADMIN 권한이 아닌 사람이 구독권을 삭제할 때, 403예외가 발생한다.")
    @WithMockUser(username = "01012345678", roles = "USER")
    @Test
    void deleteSubscriptionWithoutAdmin() throws Exception {
        // when // then
        mockMvc.perform(
                        delete("/v1/subscription/{subscriptionId}", 1L).with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}