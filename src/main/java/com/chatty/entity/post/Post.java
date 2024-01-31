package com.chatty.entity.post;

import com.chatty.entity.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    private LocalDateTime createdAt;

    private String anonymousName;

    public static Post to(User user, String title, String content) {
        return Post.builder()
                .user(user)
                .title(title)
                .content(content)
                .anonymousName("익명")
                .createdAt(LocalDateTime.now())
                .build();
    }
}
