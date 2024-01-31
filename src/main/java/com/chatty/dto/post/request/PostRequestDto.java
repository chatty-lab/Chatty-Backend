package com.chatty.dto.post.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostRequestDto {

    @NotNull(message = "글 제목은 필수로 입력해야 합니다.")
    private String title;

    @NotEmpty(message = "글 작성 내용은 필수로 입력해야 합니다.")
    private String content;
}
