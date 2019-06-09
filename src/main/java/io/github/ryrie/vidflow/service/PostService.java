package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.payload.PostRequest;
import io.github.ryrie.vidflow.payload.PostResponse;
import io.github.ryrie.vidflow.repository.PostRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.util.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);


    public Post createPost(PostRequest postRequest) {
        Post post = new Post();

        post.setWriter(postRequest.getWriter());
        post.setVideosrc(postRequest.getVideosrc());
        post.setContent(postRequest.getContent());

        Instant now = Instant.now();
        post.setRegdate(now);
        post.setUpadteddate(now);

        return postRepository.save(post);
    }

    public List<PostResponse> getAllPosts(UserPrincipal currentUser) {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
                .map(Mapper::mapPostToPostResponse)
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(UserPrincipal currrentUser, Long postId) {
        return null;
    }



}
