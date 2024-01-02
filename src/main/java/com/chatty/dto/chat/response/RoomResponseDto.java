package com.chatty.dto.chat.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDto {
    private Long roomId;
    private Long senderId;
    private Long receiverId;
}
