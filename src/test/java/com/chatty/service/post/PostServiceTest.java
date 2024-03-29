package com.chatty.service.post;

import com.chatty.constants.Authority;
import com.chatty.dto.post.request.PostRequest;
import com.chatty.dto.post.response.PostListResponse;
import com.chatty.dto.post.response.PostResponse;
import com.chatty.entity.post.Post;
import com.chatty.entity.post.PostImage;
import com.chatty.entity.user.Coordinate;
import com.chatty.entity.user.Gender;
import com.chatty.entity.user.User;
import com.chatty.repository.post.PostImageRepository;
import com.chatty.repository.post.PostRepository;
import com.chatty.repository.user.UserRepository;
import com.chatty.utils.S3Service;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private S3Service s3Service;

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostImageRepository postImageRepository;

    @AfterEach
    void tearDown() {
        postImageRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @DisplayName("게시글을 등록한다. 이미지O")
    @Test
    void createPost() throws IOException {
        // given
        User user = createUser("닉네임", "01012345678");
        userRepository.save(user);

        MockMultipartFile image1 = new MockMultipartFile("image1", "image1.jpg", "image/jpeg", new byte[]{123, 123});
        MockMultipartFile image2 = new MockMultipartFile("image2", "image2.jpg", "image/jpeg", new byte[]{123, 123});
        MockMultipartFile image3 = new MockMultipartFile("image3", "image3.jpg", "image/jpeg", new byte[]{123, 123});
        when(s3Service.uploadFileToS3(any(MultipartFile.class), anyString()))
                .thenReturn(image1.getName(), image2.getName(), image3.getName());

        PostRequest request = PostRequest.builder()
                .title("제목")
                .content("내용")
                .images(List.of(image1, image2, image3))
                .build();

        // when
        PostResponse postResponse = postService.createPost(user.getMobileNumber(), request);

        // then
        assertThat(postResponse.getId()).isNotNull();
        assertThat(postResponse.getViewCount()).isZero();
        assertThat(postResponse.getPostImages()).hasSize(3)
                .containsExactlyInAnyOrder(
                        "image1", "image2", "image3"
                );
    }
    
    @DisplayName("게시글을 등록한다. 이미지X")
    @Test
    void createPostWithoutImage() throws IOException {
        // given
        User user = createUser("닉네임", "01012345678");
        userRepository.save(user);

        PostRequest request = PostRequest.builder()
                .title("제목")
                .content("내용")
                .build();

        // when
        PostResponse postResponse = postService.createPost(user.getMobileNumber(), request);

        // then
        assertThat(postResponse.getId()).isNotNull();
    }

    @DisplayName("게시글을 단건 조회한다. 조회수도 1 증가한다.")
    @Test
    void getPost() throws IOException {
        // given
        User user = createUser("닉네임", "01012345678");
        userRepository.save(user);

        Post post = createPost("제목", "내용", user);
        postRepository.save(post);

        PostImage postImage = PostImage.builder()
                .image("image1")
                .post(post)
                .build();
        postImageRepository.save(postImage);
        post.getPostImages().add(postImage);

        // when
        PostResponse postResponse = postService.getPost(user.getMobileNumber(), post.getId());

        // then
        assertThat(postResponse.getId()).isNotNull();
        assertThat(postResponse.getTitle()).isEqualTo(post.getTitle());
        assertThat(postResponse.getPostImages()).hasSize(1)
                .containsExactlyInAnyOrder(
                        "image1"
                );
        assertThat(postResponse.getViewCount()).isEqualTo(1);
    }

    @DisplayName("게시글 목록을 조회한다.")
    @Test
    void getPostList() throws IOException {
        // given
        User user = createUser("닉네임", "01012345678");
        userRepository.save(user);

        Post post1 = createPost("제목1", "내용1", user);
        Post post2 = createPost("제목2", "내용2", user);
        Post post3 = createPost("제목3", "내용3", user);
        postRepository.saveAll(List.of(post1, post2, post3));

        // when
        List<PostListResponse> postList = postService.getPostList(user.getMobileNumber());

        // then
        assertThat(postList).hasSize(3)
                .extracting("title", "content")
                .containsExactlyInAnyOrder(
                        tuple("제목1", "내용1"),
                        tuple("제목2", "내용2"),
                        tuple("제목3", "내용3")
                );
    }

    private User createUser(final String nickname, final String mobileNumber) {
        return User.builder()
                .mobileNumber(mobileNumber)
                .deviceId("123456")
                .authority(Authority.USER)
//                .mbti(Mbti.ENFJ)
                .birth(LocalDate.now())
                .imageUrl("이미지")
                .address("주소")
                .gender(Gender.MALE)
                .nickname(nickname)
                .location(User.createPoint(
                        Coordinate.builder()
                                .lat(37.1)
                                .lng(127.1)
                                .build()
                ))
                .build();
    }

    private Post createPost(final String title, final String content, final User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}