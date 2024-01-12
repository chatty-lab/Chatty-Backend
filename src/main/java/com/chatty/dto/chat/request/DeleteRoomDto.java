package com.chatty.dto.chat.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeleteRoomDto {

    @NotBlank
    private Long roomId;

    @NotBlank
    private Long userId;
}