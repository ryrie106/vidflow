package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.domain.Comment;
import io.github.ryrie.vidflow.domain.CommentDTO;
import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.service.CommentService;
import io.github.ryrie.vidflow.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/comment")
@RestController
public class CommentController {

    private CommentService commentService;
    private PostService postService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    /* 글 번호로 댓글 읽기 */

    @GetMapping(value = "{pid}",
        produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
    )
    public ResponseEntity<List<Comment>> getComments(@PathVariable("pid") Long pid) {
        Post post = postService.getPost(pid);
        return new ResponseEntity<>(commentService.getComments(post), HttpStatus.OK);
    }

    /* 댓글 쓰기 */
    @PostMapping(value = "/write",
        consumes = "application/json",
            produces = {MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> writeComments(@RequestBody CommentDTO comment) {
        Long cid = commentService.writeComment(comment);
        return cid > 0 ? new ResponseEntity<>("success", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
