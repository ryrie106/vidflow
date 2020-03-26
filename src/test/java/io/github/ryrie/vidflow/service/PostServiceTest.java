package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.payload.PostResponse;
import io.github.ryrie.vidflow.repository.LikeRepository;
import io.github.ryrie.vidflow.repository.PostRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
//@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class PostServiceTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    LikeRepository likeRepository;

    private PostService postService;
    @BeforeEach
    public void before() {
        postService = new PostService(postRepository, userRepository, likeRepository);
    }

    @Test
    @Order(1)
    public void testCreatePost() {
        Post post = new Post();
        post.setVideosrc("video");
        post.setContent("content");
        Post p = postService.createPost(post);
        assertAll("post",
                () -> assertEquals(Long.valueOf(1), p.getId()),
                () -> assertEquals("video", p.getVideosrc()),
                () -> assertEquals("content", p.getContent()),
                () -> assertEquals(0L, p.getComments().size()));
    }

    @Test
    @Order(2)
    public void testGetPostById() {
        for(int i=2; i<=4; i++) {
            Post post = new Post();
            post.setVideosrc("video " + i);
            post.setContent("content " + i);
            postService.createPost(post);
        }
        PostResponse p2 = postService.getPostById(null, 2L);
        assertAll("post",
                () -> assertEquals(Long.valueOf(2), p2.getId()),
                () -> assertEquals("video 2", p2.getVideosrc()),
                () -> assertEquals("content 2", p2.getContent())
        );

        PostResponse p3 = postService.getPostById(null, 3L);
        assertAll("post",
                () -> assertEquals(Long.valueOf(3), p3.getId()),
                () -> assertEquals("video 3", p3.getVideosrc()),
                () -> assertEquals("content 3", p3.getContent())
        );

        PostResponse p4 = postService.getPostById(null, 4L);
        assertAll("post",
                () -> assertEquals(Long.valueOf(4), p4.getId()),
                () -> assertEquals("video 4", p4.getVideosrc()),
                () -> assertEquals("content 4", p4.getContent())
        );

    }


    @Test
    @Order(3)
    public void testGetPosts() {
        for(int i=5; i<15; i++) {
            Post post = new Post();
            post.setVideosrc("video " + i);
            post.setContent("content " + i);
            postService.createPost(post);
        }

        List<PostResponse> responses = postService.getPosts(null, 14L);
        assertEquals(Long.valueOf(14), responses.get(0).getId());
        assertEquals(Long.valueOf(13), responses.get(1).getId());
        assertEquals(Long.valueOf(12), responses.get(2).getId());
        assertEquals(Long.valueOf(11), responses.get(3).getId());
        assertEquals(Long.valueOf(10), responses.get(4).getId());

        responses = postService.getPosts(null, 9L);
        assertEquals(Long.valueOf(9), responses.get(0).getId());
        assertEquals(Long.valueOf(8), responses.get(1).getId());
        assertEquals(Long.valueOf(7), responses.get(2).getId());
        assertEquals(Long.valueOf(6), responses.get(3).getId());
        assertEquals(Long.valueOf(5), responses.get(4).getId());
    }

}
