package com.chatty.dto.chat.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    @NotNull
    private Long roomId;

    @NotNull
    private Long senderId;

    @NotBlank
    private String content;

    public static MessageDto to(MessageDto messageDto){
        return MessageDto.builder()
                .roomId(messageDto.roomId)
                .senderId(messageDto.senderId)
                .content(messageDto.content)
                .build();
    }
}
