package com.chatty.entity.chat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chatRoom")
public class ChatRoom {

    @Id
    @Column(name = "room_id")
    private long id;

    private String name;

    private Long userCount;

    @OneToMany(mappedBy = "chatRoom")
    private Set<ChatRoomUser> users = new HashSet<>();

    @OneToMany(mappedBy = "chatRoom")
    private Set<ChatRoomUser> messages = new HashSet<>();
}
