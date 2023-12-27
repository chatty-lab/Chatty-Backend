package com.chatty.entity.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chatRoom")
public class ChatRoom {

    @Id
    @Column(name = "room_id")
    private String id;

    @OneToMany(mappedBy = "chatRoom")
    private Set<ChatRoomUser> users = new HashSet<>();

    public static ChatRoom create(){
        return ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .build();
    }
}
