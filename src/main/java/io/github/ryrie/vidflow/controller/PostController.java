package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/posts")
@RestController
public class PostController {

    private PostService service;

    @Autowired
    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping(value = "/{postno}",
            produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
    )
    public Post getPost(@PathVariable("postno") Long bno) {
        return service.getPost(bno);
    }

    @GetMapping(value = "/all",
            produces = { MediaType.APPLICATION_JSON_UTF8_VALUE }
    )
    public List<Post> getPostAll() {
        return service.getPostAll();
    }

    @PostMapping(value = "/write",
            consumes = "application/json",
            produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> writePost(@RequestBody Post post) {
        Long postno = service.writePost(post);

        return postno > 0 ? new ResponseEntity<>("success", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
