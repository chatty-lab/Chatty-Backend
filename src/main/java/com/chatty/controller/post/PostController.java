package com.chatty.controller.post;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.post.request.PostRequestDto;
import com.chatty.dto.post.response.PostResponseDto;
import com.chatty.service.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("post")
public class PostController {

    private final PostService postService;

    @PostMapping("/write")
    public ApiResponse<PostResponseDto> createPosts(@Valid @RequestBody PostRequestDto postRequestDto, Authentication authentication){
        log.info("게시물 작성");
        return ApiResponse.ok(postService.createPost(postRequestDto, authentication));
    }

}
