package com.chatty.dto.chat.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ChatDto {
    @NotNull
    private Long userId; // 채팅을 받는 사람
}
