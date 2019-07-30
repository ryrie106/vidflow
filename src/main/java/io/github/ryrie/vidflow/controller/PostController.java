package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.payload.ApiResponse;
import io.github.ryrie.vidflow.payload.PostRequest;
import io.github.ryrie.vidflow.payload.PostResponse;
import io.github.ryrie.vidflow.payload.QueryPostsResponse;
import io.github.ryrie.vidflow.security.CurrentUser;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.service.NotificationService;
import io.github.ryrie.vidflow.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
public class PostController {

    private PostService postService;
    private NotificationService notificationService;

    @Autowired
    public PostController(PostService postService, NotificationService notificationService) {
        this.postService = postService;
        this.notificationService = notificationService;
    }


    @GetMapping(value="/posts/postId")
    public ResponseEntity<?> getPostId() {
        Long pid = postService.getPostId();
        return ResponseEntity.ok().body(new ApiResponse(true, pid.toString()));
    }

    @GetMapping(value = "/posts/{postId}")
    public PostResponse getPostById(@CurrentUser UserPrincipal currentUser, @PathVariable("postId") Long postId) {
        return postService.getPostById(currentUser, postId);
    }

    @GetMapping(value = "/posts")
    public List<PostResponse> getPosts(@CurrentUser UserPrincipal currentUser, @RequestParam("id") Long id, @RequestParam("page") Long page) {
        return postService.getPosts(currentUser, id, page);
    }

    @PostMapping(value = "/posts")
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public ResponseEntity<?> createPost(@CurrentUser UserPrincipal currentUser, @RequestBody PostRequest postRequest) {
        Post post = postService.createPost(currentUser, postRequest);
        notificationService.newPostNotify(currentUser, post.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{postId}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Post Created Successfully"));
    }

    @DeleteMapping(value = "/posts/{postId}")
    @PreAuthorize("hasRole('USER')")
    ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        // TODO: deletePost에 실패하면?
        return ResponseEntity.ok().body(new ApiResponse(true, "Post Deleted Successfully"));
//        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/posts/like/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> likePost(@CurrentUser UserPrincipal currentUser, @PathVariable Long postId) {
        Post post = postService.likePost(currentUser, postId);
        notificationService.likePostNotify(currentUser, post);
        return ResponseEntity.ok().body(new ApiResponse(true, "Post like Successfully"));
    }


    @GetMapping(value = "/posts/user/{userId}")
    public List<QueryPostsResponse> getUserPosts(@PathVariable Long userId) {
        return postService.getUserPosts(userId);
    }

    @GetMapping(value = "/posts/likes/{userId}")
    public List<QueryPostsResponse> getUserLikes(@PathVariable Long userId) {
        return postService.getUserLikes(userId);
    }

    @DeleteMapping(value = "/posts/like/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unlikePost(@CurrentUser UserPrincipal currentUser, @PathVariable Long postId) {
        postService.unlikePost(currentUser, postId);
        return ResponseEntity.ok().body(new ApiResponse(true, "Post like Successfully"));
    }

    @GetMapping(value = "/posts/query")
    public List<QueryPostsResponse> queryPostContent(@RequestParam("content") String content) {
        return postService.queryPostContent(content);
    }

}
