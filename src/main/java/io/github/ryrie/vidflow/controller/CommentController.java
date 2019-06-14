package io.github.ryrie.vidflow.controller;


import io.github.ryrie.vidflow.domain.Comment;
import io.github.ryrie.vidflow.payload.ApiResponse;
import io.github.ryrie.vidflow.payload.CommentRequest;
import io.github.ryrie.vidflow.payload.CommentResponse;
import io.github.ryrie.vidflow.security.CurrentUser;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.service.CommentService;
import io.github.ryrie.vidflow.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/comment")
@RestController
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/{postId}")
    public List<CommentResponse> getComments(@CurrentUser UserPrincipal currentUser, @PathVariable("postId") Long postId) {
        return commentService.getCommentsByPostId(currentUser, postId);
    }

    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@RequestBody CommentRequest commentRequest,
                                           @PathVariable("postId") Long postId) {
        Comment comment = commentService.createComment(commentRequest, postId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{commentId}")
                .buildAndExpand(comment.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Comment Created Successfully"));
    }



}
