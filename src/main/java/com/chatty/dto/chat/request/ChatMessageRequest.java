package com.chatty.dto.chat.request;

import com.chatty.entity.chat.ChatMessage;
import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChatMessageRequest {

    @NotBlank(message = "채팅 메세지는 입력해야 됩니다.")
    private String content;

    @Builder
    public ChatMessageRequest(final String content) {
        this.content = content;
    }

    public ChatMessage toEntity(final User sender, final User receiver, final ChatRoom chatRoom, final LocalDateTime now) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .isRead(false)
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .sendTime(now)
                .build();
    }
}
