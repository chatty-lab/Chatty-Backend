package com.chatty.entity.chat;

import com.chatty.entity.user.User;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatRoom_users")
public class ChatRoomUser {

    @EmbeddedId
    private ChatRoomUserId id = new ChatRoomUserId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;
}
