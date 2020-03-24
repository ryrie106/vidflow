package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.*;
import io.github.ryrie.vidflow.exception.AppException;
import io.github.ryrie.vidflow.payload.PostRequest;
import io.github.ryrie.vidflow.payload.PostResponse;
import io.github.ryrie.vidflow.payload.QueryPostsResponse;
import io.github.ryrie.vidflow.repository.LikeRepository;
import io.github.ryrie.vidflow.repository.PostRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.util.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private LikeRepository likeRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository,
                       LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.likeRepository = likeRepository;
    }

    public Post createPost(PostRequest postRequest) {
        Post post = new Post();
        post.setThumbnailsrc(postRequest.getThumbnailSrc());
        post.setVideosrc(postRequest.getVideoSrc());
        post.setContent(postRequest.getContent());
        return createPost(post);
    }

    public Post createPost(User user, PostRequest postRequest) {
        return null;
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public PostResponse getPostById(User user, Long postId) {
//        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("findById during getPostByid"));
        Optional<Post> post = postRepository.findById(postId);
        assert(post.isPresent());
        return Mapper.mapPostToPostResponse(post.get(), user);
    }

    public List<PostResponse> getPosts(User user, Long postId) {
//        PageRequest pageRequest = PageRequest.of(page.intValue(), 5, Sort.by("id").descending());
        List<Post> posts = postRepository.findTop5ByIdLessThanEqualOrderByIdDesc(postId);
        assert(posts!=null);
        return posts.stream().map(post -> Mapper.mapPostToPostResponse(post, user)).collect(Collectors.toList());
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("Delete Post"));
        postRepository.delete(post);
    }

    @Transactional
    public Post likePost(UserPrincipal currentUser, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("Post"));
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new AppException("findById during likePost"));
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
        User writer = post.getWriter();
        // TODO: 경쟁조건이 될 수 있을까
        writer.setNum_liked(writer.getNum_liked()+1);
        return post;
    }

    @Transactional
    public void unlikePost(UserPrincipal currentUser, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("Post"));
        User user = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(()->new AppException("findByEmail during unlikePost"));
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow(() -> new AppException("findByPostAndUser during unlikePost"));
        likeRepository.delete(like);
        User writer = post.getWriter();
        // TODO: 경쟁조건이 될 수 있을까?
        writer.setNum_liked(writer.getNum_liked()-1);
//        return post;
    }

    public List<QueryPostsResponse> getUserPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("findById during getUserPosts"));
        List<Post> userPosts = user.getPosts();
        return userPosts.stream().map(post -> {
            QueryPostsResponse response = new QueryPostsResponse();
            response.setPostId(post.getId());
            response.setThumbnailSrc(post.getThumbnailsrc());
            return response;
        }).collect(Collectors.toList());
    }

    public List<QueryPostsResponse> getUserLikes(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("findById during getUserLikes"));
        List<Like> userLikes = user.getLikes();
        return userLikes.stream().map(like -> {
            QueryPostsResponse response = new QueryPostsResponse();
            response.setPostId(like.getPost().getId());
            response.setThumbnailSrc(like.getPost().getThumbnailsrc());
            return response;
        }).collect(Collectors.toList());
    }

    public List<QueryPostsResponse> queryPostContent(String content) {
        List<Post> posts = postRepository.findByContentContaining(content);
        return posts.stream().map(post -> {
            QueryPostsResponse response = new QueryPostsResponse();
            response.setPostId(post.getId());
            response.setThumbnailSrc(post.getThumbnailsrc());
            return response;
        }).collect(Collectors.toList());
    }
}
