package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.exception.AppException;
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
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public Post createPost(PostRequest postRequest) {
        Post post = new Post();

        post.setVideosrc(postRequest.getVideoSrc());
        post.setContent(postRequest.getContent());
        return postRepository.save(post);
    }

    /**
     * Repository에서 post를 찾고 mapPostToPostResponse를 사용하여
     */
    public List<PostResponse> getAllPosts() {
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

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("Post"));
        postRepository.delete(post);

    }

    public PostResponse getPostById(Long postId) {
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
