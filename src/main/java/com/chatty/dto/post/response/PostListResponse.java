package com.chatty.dto.post.response;

import com.chatty.entity.post.Post;
import com.chatty.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PostListResponse {

    private Long postId;
    private String title;
    private String content;
    private int viewCount;
    private LocalDateTime createdAt;

    private Long userId;
    private String nickname;
    private String imageUrl;

    @Builder
    public PostListResponse(final Long postId, final String title, final String content, final int viewCount, final LocalDateTime createdAt, final Long userId, final String nickname, final String imageUrl) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
        this.userId = userId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }

    public static PostListResponse of(final Post post) {
        return PostListResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .viewCount(post.getViewCount())
                .createdAt(post.getCreatedAt())
                .userId(post.getUser().getId())
                .nickname(post.getUser().getNickname())
                .imageUrl(post.getUser().getImageUrl())
                .build();
    }
}
