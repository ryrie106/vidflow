package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.payload.ApiResponse;
import io.github.ryrie.vidflow.payload.PostRequest;
import io.github.ryrie.vidflow.payload.PostResponse;
import io.github.ryrie.vidflow.security.CurrentUser;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/posts")
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping(value = "/{postId}")
    public PostResponse getPostById(@CurrentUser UserPrincipal currentUser, @PathVariable("postId") Long postId) {
        return postService.getPostById(currentUser, postId);
    }

    @GetMapping
    public List<PostResponse> getPosts(@CurrentUser UserPrincipal currentUser) {
        return postService.getAllPosts(currentUser);
    }

    @PostMapping(value = "/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> createPost(@RequestBody PostRequest postRequest) {
        Post post = postService.createPost(postRequest);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{postId}")
                .buildAndExpand(post.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Post Created Successfully"));
    }
}
