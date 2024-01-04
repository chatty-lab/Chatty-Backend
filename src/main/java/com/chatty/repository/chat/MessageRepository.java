package com.chatty.repository.chat;

import com.chatty.entity.chat.ChatMessage;
import com.chatty.entity.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, Long> {

    Optional<ChatMessage> findChatMessagesByMessageId(Long messageId);

    Optional<List<ChatMessage>> findByIsReadFalseAndReceiverAndSenderOrderBySendTimeDesc(User receiver, User sender);
}
