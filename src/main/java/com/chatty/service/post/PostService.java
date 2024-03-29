package com.chatty.service.post;

import com.chatty.constants.Code;
import com.chatty.dto.post.request.PostRequest;
import com.chatty.dto.post.response.PostListResponse;
import com.chatty.dto.post.response.PostResponse;
import com.chatty.entity.post.Post;
import com.chatty.entity.post.PostImage;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.repository.post.PostImageRepository;
import com.chatty.repository.post.PostRepository;
import com.chatty.repository.user.UserRepository;
import com.chatty.utils.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;
    private final S3Service s3service;

    @Transactional
    public PostResponse createPost(final String mobileNumber, final PostRequest request) throws IOException {
        log.info("createPost Method Start");
        User user = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));

        Post post = postRepository.save(request.toEntity(user));

        if (request.getImages() == null || request.getImages().isEmpty()) {
            return PostResponse.of(post, user);
        }


        for (MultipartFile image : request.getImages()) {
            validateExtension(image.getOriginalFilename());
            String fileUrl =
                    s3service.uploadFileToS3(image, "post/" + post.getId() + "/" + image.getOriginalFilename());
            PostImage postImage = PostImage.builder()
                    .post(post)
                    .image(fileUrl)
                    .build();
            PostImage savedPostImage = postImageRepository.save(postImage);
            post.getPostImages().add(savedPostImage);
        }

        return PostResponse.of(post, user);
    }

    @Transactional
    public PostResponse getPost(final String mobileNumber, final Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_POST));

        post.addViewCount();

        return PostResponse.of(post, post.getUser());
    }

    public List<PostListResponse> getPostList(final String mobileNumber) {
        List<Post> postList = postRepository.findAll(Sort.by("id").descending());

        return postList.stream()
                .map(PostListResponse::of)
                .collect(Collectors.toList());
    }

    private void validateExtension(final String filename) {
        String[] file = filename.split("\\.");
        String extension = file[file.length - 1];

        if (!extension.equals("jpg") && !extension.equals("jpeg") && !extension.equals("png")) {
            throw new CustomException(Code.INVALID_EXTENSION);
        }
    }
}
