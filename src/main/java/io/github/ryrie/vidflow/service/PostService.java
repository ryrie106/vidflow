package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.payload.PostRequest;
import io.github.ryrie.vidflow.payload.PostResponse;
import io.github.ryrie.vidflow.repository.PostRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.util.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);


    public Post createPost(PostRequest postRequest) {
        Post post = new Post();

        post.setVideosrc(postRequest.getVideosrc());
        post.setContent(postRequest.getContent());
        return postRepository.save(post);
    }

    public List<PostResponse> getAllPosts(UserPrincipal currentUser) {
        List<Post> posts = postRepository.findAll();

        Map<Long, User> writerMap = getPostWriterMap(posts);
        return posts.stream()
                .map(post ->
                    Mapper.mapPostToPostResponse(post,
                            writerMap.get(post.getWriter())
                    )
                )
                .collect(Collectors.toList());
    }

    public PostResponse getPostById(UserPrincipal currrentUser, Long postId) {
        return null;
    }

    private Map<Long, User> getPostWriterMap(List<Post> posts) {
        List<Long> writerIds = posts.stream()
                .map(Post::getWriter)
                .distinct()
                .collect(Collectors.toList());

        List<User> writers = userRepository.findByIdIn(writerIds);
        return writers.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }


}
