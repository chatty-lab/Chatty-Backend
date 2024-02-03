package com.chatty.controller.post;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.post.request.PostRequestDto;
import com.chatty.dto.post.response.PostResponseDto;
import com.chatty.dto.post.response.PostsResponseDto;
import com.chatty.service.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post/write")
    public ApiResponse<PostResponseDto> createPost(@Valid @RequestBody PostRequestDto postRequestDto, Authentication authentication){
        log.info("게시물 작성");
        return ApiResponse.ok(postService.createPost(postRequestDto, authentication));
    }

    @GetMapping("/posts")
    public ApiResponse<PostsResponseDto> getPosts(
            @RequestParam(required = false, defaultValue = "0", value = "page") int page,
            @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria
    ){
        log.info("게시물 조회");
        return ApiResponse.ok(postService.getPosts(page,criteria));
    }

}
