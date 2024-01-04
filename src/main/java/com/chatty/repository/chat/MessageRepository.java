package com.chatty.repository.chat;

import com.chatty.entity.chat.ChatMessage;
import com.chatty.entity.chat.ChatRoom;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findChatMessagesByChatRoom(ChatRoom chatRoom);
    Optional<ChatMessage> findChatMessagesByMessageId(Long messageId);
}
