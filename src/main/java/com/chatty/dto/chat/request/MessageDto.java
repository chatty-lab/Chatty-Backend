package com.chatty.dto.chat.request;

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

    @NotNull(message = "roomId는 필수로 입력해야 합니다.")
    private Long roomId;

    @NotNull(message = "senderId(송신자)는 필수로 입력해야 합니다.")
    private Long senderId;

    @NotNull(message = "receiverId(수신자)는 필수로 입력해야 합니다.")
    private Long receiverId;

    @NotBlank(message = "content(내용)은 필수로 입력해야 합니다.")
    private String content;
}
