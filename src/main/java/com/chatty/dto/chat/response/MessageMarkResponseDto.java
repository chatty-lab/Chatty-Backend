package com.chatty.dto.chat.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageMarkResponseDto {

    private Long messageId;
    private Boolean isRead;

    public static MessageMarkResponseDto of(Long messageId, Boolean isRead){
        return MessageMarkResponseDto.builder()
                .messageId(messageId)
                .isRead(isRead)
                .build();
    }
}
