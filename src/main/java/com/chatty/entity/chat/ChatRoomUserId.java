package com.chatty.entity.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ChatRoomUserId implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "room_id")
    private Long roomId;
}
