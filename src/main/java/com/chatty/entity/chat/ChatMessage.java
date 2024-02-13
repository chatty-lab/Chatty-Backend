package com.chatty.entity.chat;

import com.chatty.entity.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime sendTime;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @Column(nullable = false)
    private Boolean isRead;

    public static ChatMessage to(ChatRoom chatRoom, User sender, User receiver, String content) {
        return ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .sendTime(LocalDateTime.now())
                .isRead(false)
                .build();
    }

    public void setIsRead() {
        this.isRead = !this.isRead;
    }
}
