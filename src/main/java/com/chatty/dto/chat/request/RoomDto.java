package com.chatty.dto.chat.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class RoomDto {
    @NotNull
    private Long senderId; // 보내는 사람

    @NotNull
    private Long receiverId; // 받는 사람
}
