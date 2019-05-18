package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Long savePost(Post post) {
        postRepository.save(post);
        return post.getPostno();
    }

    public Post getPost(Long postno) {
        return postRepository.findById(postno).get();
    }

    public List<Post> getPostAll() {
        return postRepository.findAll();
    }

}
