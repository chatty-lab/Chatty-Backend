package com.chatty.repository.chat;

import com.chatty.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeassageRepository extends JpaRepository<ChatMessage, Long> {

}
