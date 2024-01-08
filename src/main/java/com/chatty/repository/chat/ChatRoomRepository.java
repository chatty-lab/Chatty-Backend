package com.chatty.repository.chat;

import com.chatty.entity.chat.ChatRoom;
import com.chatty.entity.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    Optional<ChatRoom> findChatRoomBySenderAndReceiver(User sender, User receiver);
    Optional<ChatRoom> findChatRoomByReceiverAndSender(User receiver, User sender);
    Optional<ChatRoom> findChatRoomByRoomId(Long roomId);
}
