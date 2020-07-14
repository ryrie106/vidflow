package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.payload.ApiResponse;
import io.github.ryrie.vidflow.payload.PostRequest;
import io.github.ryrie.vidflow.payload.PostResponse;
import io.github.ryrie.vidflow.payload.QueryPostsResponse;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.service.NotificationService;
import io.github.ryrie.vidflow.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RequestMapping("/posts")
@RestController
@Slf4j
public class PostController {

    private final PostService postService;
    private final NotificationService notificationService;

    @GetMapping
    public List<PostResponse> getPosts(@AuthenticationPrincipal UserPrincipal currentUser, @RequestParam("id") Long id) {
        // TODO:
        return postService.getPosts(currentUser == null ? null : currentUser.getDomain(), id);
    }

//    @GetMapping("/postId")
//    public ResponseEntity<?> getPostId() {
//        Long pid = postService.getPostId();
//        return ResponseEntity.ok().body(new ApiResponse(true, pid.toString()));
//    }

    @GetMapping("/{postId}")
    public PostResponse getPostById(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable("postId") Long postId) {
        return postService.getPostById(currentUser == null ? null : currentUser.getDomain(), postId);
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public ResponseEntity<?> createPost(@AuthenticationPrincipal UserPrincipal currentUser, @RequestBody PostRequest postRequest) {
        Post post = postService.createPost(postRequest);
        notificationService.newPostNotify(currentUser, post.getId());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{postId}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Post Created Successfully"));
    }

    @DeleteMapping("/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deletePost(@PathVariable Long postId) {
        postService.deletePost(postId);
        // TODO: deletePost에 실패하면?
        return ResponseEntity.ok().body(new ApiResponse(true, "Post Deleted Successfully"));
//        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{postId}/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> likePost(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long postId) {
        Post post = postService.likePost(currentUser, postId);
        notificationService.likePostNotify(currentUser, post);
        return ResponseEntity.ok().body(new ApiResponse(true, "Post like Successfully"));
    }

    @DeleteMapping("/{postId}/like")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> unlikePost(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long postId) {
        postService.unlikePost(currentUser, postId);
        return ResponseEntity.ok().body(new ApiResponse(true, "Post like Successfully"));
    }


    @GetMapping("/query")
    public List<QueryPostsResponse> queryPostContent(@RequestParam("content") String content) {
        return postService.queryPostContent(content);
    }

}
