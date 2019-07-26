package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Like;
import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.exception.AppException;
import io.github.ryrie.vidflow.payload.PostRequest;
import io.github.ryrie.vidflow.payload.PostResponse;
import io.github.ryrie.vidflow.payload.UserPostsResponse;
import io.github.ryrie.vidflow.repository.CommentRepository;
import io.github.ryrie.vidflow.repository.LikeRepository;
import io.github.ryrie.vidflow.repository.PostRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.util.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostService {

    private PostRepository postRepository;
    private UserRepository userRepository;
    private LikeRepository likeRepository;

    @Autowired
    public PostService(PostRepository postRepository,
                       UserRepository userRepository, LikeRepository likeRepository) {
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

    public Long getPostId() {
        return postRepository.getMaxId();
    }

    public PostResponse getPostById(Long postId) {
//        Post post = postRepository.findById(postId);
        return null;
    }

    public List<PostResponse> getPosts(UserPrincipal currentUser, Long id, Long page) {
        PageRequest pageRequest = PageRequest.of(page.intValue(), 5, Sort.by("id").descending());
        Page<Post> posts = postRepository.findByIdLessThanEqual(id, pageRequest);

        // TODO: 좀 억지스러운 코드인데 다른 방법이 없을까
        if(currentUser == null)
            return posts.stream().map(post -> Mapper.mapPostToPostResponse(post, null)).collect(Collectors.toList());

        return posts.stream().map(post -> Mapper.mapPostToPostResponse(post, currentUser.getDomain())).collect(Collectors.toList());
    }

    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("Delete Post"));
        postRepository.delete(post);

    }


    @Transactional
    public void likePost(UserPrincipal currentUser, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("Post"));
        User user = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(()->new AppException("findByEmail during likePost"));
        Like like = new Like();
        like.setPost(post);
        like.setUser(user);
        likeRepository.save(like);
        User writer = post.getWriter();
        writer.setNum_liked(writer.getNum_liked()+1);
    }

    @Transactional
    public void unlikePost(UserPrincipal currentUser, Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("Post"));
        User user = userRepository.findByEmail(currentUser.getEmail()).orElseThrow(()->new AppException("findByEmail during unlikePost"));
        Like like = likeRepository.findByPostAndUser(post, user).orElseThrow(() -> new AppException("findByPostAndUser during unlikePost"));
        likeRepository.delete(like);
        User writer = post.getWriter();
        writer.setNum_liked(writer.getNum_liked()-1);
    }

    public List<UserPostsResponse> getUserPosts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("findById during getUserPosts"));
        List<Post> userPosts = user.getPosts();
        return userPosts.stream().map(post -> {
            UserPostsResponse response = new UserPostsResponse();
            response.setPostId(post.getId());
            response.setThumbnailSrc(post.getThumbnailsrc());
            return response;
        }).collect(Collectors.toList());
    }

    public List<UserPostsResponse> getUserLikes(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("findById during getUserLikes"));
        List<Like> userLikes = user.getLikes();
        return userLikes.stream().map(like -> {
            UserPostsResponse response = new UserPostsResponse();
            response.setPostId(like.getPost().getId());
            response.setThumbnailSrc(like.getPost().getThumbnailsrc());
            return response;
        }).collect(Collectors.toList());
    }

}
