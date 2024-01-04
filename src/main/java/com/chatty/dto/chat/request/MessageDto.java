package com.chatty.dto.chat.request;

import com.chatty.entity.chat.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {

    @NotNull
    private Long roomId;

    @NotNull
    private Long senderId;

    @NotNull
    private Long receiverId;

    @NotBlank
    private String content;

    public static MessageDto to(ChatMessage chatMessage){
        return MessageDto.builder()
                .roomId(chatMessage.getChatRoom().getRoomId())
                .senderId(chatMessage.getSender().getId())
                .receiverId(chatMessage.getReceiver().getId())
                .content(chatMessage.getContent())
                .build();
    }
}
