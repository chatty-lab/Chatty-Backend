package com.chatty.dto.post.response;

import com.chatty.entity.post.Post;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class PostsResponseDto {

    private List<Post> posts = new ArrayList<>();

    public static PostsResponseDto of(Page page) {
        return PostsResponseDto.builder()
                .posts(page.getContent())
                .build();
    }

}
