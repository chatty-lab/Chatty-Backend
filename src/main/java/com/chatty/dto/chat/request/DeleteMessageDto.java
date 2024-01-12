package com.chatty.dto.chat.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class DeleteMessageDto {

    @NotBlank
    private Long roomId;

    @NotBlank
    private Long userId;
}
