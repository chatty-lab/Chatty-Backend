package com.chatty.dto.chat.response;

import com.chatty.entity.chat.ChatMessage;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MultipleMessageResponseDto {

    @NotBlank(message = "content(내용)은 필수로 입력해야 합니다.")
    private List<ChatMessage> contents;

    public static MultipleMessageResponseDto of(List<ChatMessage> messages){
        return MultipleMessageResponseDto.builder()
                .contents(messages)
                .build();
    }
}