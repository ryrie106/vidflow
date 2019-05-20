package io.github.ryrie.vidflow;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
        Post post1 = Post.createPost("one", "http://ec2-13-125-253-101.ap-northeast-2.compute.amazonaws.com/videos/bunny.mp4", "Hello world!");
        posts.save(post1);
        Post post2 = Post.createPost("two", "http://ec2-13-125-253-101.ap-northeast-2.compute.amazonaws.com/videos/bunny.mp4", "foobarwar");
        posts.save(post2);
        Post post3 = Post.createPost("three", "http://ec2-13-125-253-101.ap-northeast-2.compute.amazonaws.com/videos/bunny.mp4", "rheha");
        posts.save(post3);
        Post post4 = Post.createPost("four", "http://ec2-13-125-253-101.ap-northeast-2.compute.amazonaws.com/videos/bunny.mp4", "zzzzzzzzzzzzzzzzz");
        posts.save(post4);

    }
}
