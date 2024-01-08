package com.chatty.dto.chat.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UnreadMessageDto {

    @NotNull
    private Long roomId;

    @NotNull
    private Long receiverId;

    @NotNull
    private Long senderId;
}
