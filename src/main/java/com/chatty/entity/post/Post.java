package com.chatty.entity.post;

import com.chatty.entity.BaseTimeEntity;
import com.chatty.entity.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "update Post SET deleted_at = current_timestamp where post_id = ?")
@SQLRestriction("deleted_at is null")
@Entity
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private String content;

    private int viewCount;

    @OneToMany(mappedBy = "post")
    private List<PostImage> postImages = new ArrayList<>();

    @Builder
    public Post(final User user, final String title, final String content, final int viewCount) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
    }

    public void addViewCount() {
        this.viewCount++;
        // 테스트 코드 작성?
    }
}
