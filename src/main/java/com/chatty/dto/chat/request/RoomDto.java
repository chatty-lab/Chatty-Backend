package com.chatty.dto.chat.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomDto {
    @NotNull(message = "senderId(송신자)는 필수로 입력해야 합니다.")
    private Long senderId; // 보내는 사람

    @NotNull(message = "receiverId(수신자)는 필수로 입력해야 합니다.")
    private Long receiverId; // 받는 사람
}
