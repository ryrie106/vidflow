package io.github.ryrie.vidflow;


import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

/**
 * pre-load some data
 */

@Component
public class DatabaseLoader implements CommandLineRunner {

    private final PostRepository posts;

    @Autowired
    public DatabaseLoader(PostRepository posts) {
        this.posts = posts;
    }

    @Override
    public void run(String... strings) throws Exception {
        Post post1 = this.posts.save(new Post(1L, "one", "http:/localhost/source.mp4", "Hello world!", 0, Date.from(Instant.now()), Date.from(Instant.now())));
        Post post2 = this.posts.save(new Post(2L, "two", "http:/localhost/source.mp4", "foo", 0, Date.from(Instant.now()), Date.from(Instant.now())));
        Post post3 = this.posts.save(new Post(3L, "three", "http:/localhost/source.mp4", "bar", 0, Date.from(Instant.now()), Date.from(Instant.now())));
        Post post4 = this.posts.save(new Post(4L, "four", "http:/localhost/source.mp4", "bzb", 0, Date.from(Instant.now()), Date.from(Instant.now())));
    }
}
