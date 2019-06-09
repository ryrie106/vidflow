package io.github.ryrie.vidflow.controller;


import io.github.ryrie.vidflow.service.CommentService;
import io.github.ryrie.vidflow.service.PostService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comment")
@RestController
public class CommentController {

    private CommentService commentService;
    private PostService postService;


}
