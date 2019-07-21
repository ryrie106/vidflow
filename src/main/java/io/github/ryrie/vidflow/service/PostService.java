package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Like;
import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.exception.AppException;
import io.github.ryrie.vidflow.payload.PostRequest;
import io.github.ryrie.vidflow.payload.PostResponse;
import io.github.ryrie.vidflow.repository.CommentRepository;
import io.github.ryrie.vidflow.repository.LikeRepository;
import io.github.ryrie.vidflow.repository.PostRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.util.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;
    private LikeRepository likeRepository;

    @Autowired
    public PostService(CommentRepository commentRepository, PostRepository postRepository,
                       UserRepository userRepository, LikeRepository likeRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
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
    public List<PostResponse> getAllPosts(UserPrincipal currentUser) {
        List<Post> posts = postRepository.findAll();

        List<Long> likePostIds;

        if(currentUser != null) {
            // 현재 유저의 like 정보를 찾아 like한 post의 id를 리스트로 매핑후 postResponse에 넘겨준다.
            User user = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(() -> new AppException("findByEmail during getAllPosts"));
            List<Like> likes = likeRepository.findAllByUser(user);
            likePostIds = likes.stream().map(like -> like.getPost().getId()).collect(Collectors.toList());
        } else {
            likePostIds = new ArrayList<>();
        }

        Map<Long, User> writerMap = getPostWriterMap(posts);
        return posts.stream()
                .map(post ->
                    Mapper.mapPostToPostResponse(
                            post,
                            likePostIds,
                            commentRepository.countByPost(post.getId()),
                            likeRepository.countByPost(post),
                            writerMap.get(post.getWriter())
                    )
                )
                .collect(Collectors.toList());
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("Delete Post"));
        postRepository.delete(post);

    }


    public void likePost(UserPrincipal currentUser, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("Post"));
        User user = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(()->new AppException("findByEmail during likePost"));
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
    }

    public void unlikePost(UserPrincipal currentUser, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("Post"));
        User user = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(()->new AppException("findByEmail during unlikePost"));
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow(() -> new AppException("findByPostAndUser during unlikePost"));
        likeRepository.delete(like);
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
