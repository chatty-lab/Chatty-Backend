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
    @NotNull
    private Long senderId; // 보내는 사람

    @NotNull
    private Long receiverId; // 받는 사람
}
