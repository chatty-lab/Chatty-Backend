package com.chatty.dto.chat.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UnreadMessageDto {

    @NotNull(message = "roomId(방 번호)는 필수로 입력해야 합니다.")
    private Long roomId;
}
