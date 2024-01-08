package com.chatty.dto.chat.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UnreadMessageDto {

    @NotNull(message = "roomId(방 번호)는 필수로 입력해야 합니다.")
    private Long roomId;

    @NotNull(message = "receiverId(수신자)는 필수로 입력해야 합니다.")
    private Long receiverId;

    @NotNull(message = "senderId(송신자)는 필수로 입력해야 합니다.")
    private Long senderId;
}
