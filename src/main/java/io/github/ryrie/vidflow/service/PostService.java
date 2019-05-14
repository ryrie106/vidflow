package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;


    public Long SavePost(Post post) {
        postRepository.save(post);
        return post.getPostno();
    }

    public Post GetPost(Long postno) {
        return postRepository.findById(postno).get();
    }

}
