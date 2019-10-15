package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.domain.Comment;
import io.github.ryrie.vidflow.payload.ApiResponse;
import io.github.ryrie.vidflow.payload.CommentRequest;
import io.github.ryrie.vidflow.payload.CommentResponse;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.service.CommentService;
import io.github.ryrie.vidflow.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/comments")
@RestController
public class CommentController {

    private CommentService commentService;
    private NotificationService notificationService;

    @Autowired
    public CommentController(CommentService commentService, NotificationService notificationService) {
        this.commentService = commentService;
        this.notificationService = notificationService;
    }

    @GetMapping("/{postId}")
    public List<CommentResponse> getComments(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable("postId") Long postId) {
        return commentService.getCommentsByPostId(currentUser, postId);
    }

    @PostMapping("/{postId}")
    @PreAuthorize("hasRole('USER')")
    @Transactional
    public ResponseEntity<?> createComment(@AuthenticationPrincipal UserPrincipal currentUser,
                                           @RequestBody CommentRequest commentRequest,
                                           @PathVariable("postId") Long postId) {
        Comment comment = commentService.createComment(commentRequest, postId);
        notificationService.commentNotify(currentUser, comment);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("{commentId}")
                .buildAndExpand(comment.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Comment Created Successfully"));
    }

    @DeleteMapping("/{postId}/{commentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteComment(@AuthenticationPrincipal UserPrincipal currentUser,
                                           @PathVariable("postId") Long postId,
                                           @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.ok().body(new ApiResponse(true, "Comment Deleted Successfully"));
    }



}
