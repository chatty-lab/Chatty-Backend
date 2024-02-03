package com.chatty.service.post;

import com.chatty.constants.Code;
import com.chatty.dto.post.request.PostRequestDto;
import com.chatty.dto.post.response.PostResponseDto;
import com.chatty.dto.post.response.PostsResponseDto;
import com.chatty.entity.post.Post;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.post.PostRepository;
import com.chatty.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final int PAGE_SIZE = 10;

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, Authentication authentication){

        User user = userRepository.findUserByMobileNumber(authentication.getName()).orElseThrow(() -> new CustomException(
                Code.NOT_EXIST_USER));
        Post post = Post.to(user, postRequestDto.getTitle(), postRequestDto.getContent());
        postRepository.save(post);

        return PostResponseDto.of(post);
    }

    public PostsResponseDto getPosts(int page, String criteria){
        Pageable pageable = PageRequest.of(page, PAGE_SIZE, Sort.by(Sort.Direction.DESC, criteria));
        Page<PostResponseDto> posts = postRepository.findAll(pageable).map(PostResponseDto::of);

        return PostsResponseDto.of(posts);
    }
}
