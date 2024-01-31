package com.chatty.dto.post.response;

import com.chatty.entity.post.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostResponseDto {
    private Long postId;
    private String title;
    private String content;

    public static PostResponseDto of(Post post){
        return PostResponseDto.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }
}
