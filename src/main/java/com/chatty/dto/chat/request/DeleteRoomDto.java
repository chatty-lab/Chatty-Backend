package com.chatty.dto.chat.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteRoomDto {

    @NotNull(message = "roomId(방 아이디)는 필수로 입력해야 합니다.")
    private Long roomId;

    @NotNull(message = "userId(송신자) 는 필수로 입력해야 합니다.")
    private Long userId;
}
