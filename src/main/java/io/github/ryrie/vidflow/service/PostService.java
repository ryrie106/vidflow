package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.domain.PostDTO;
import io.github.ryrie.vidflow.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    /* 글 읽기 */
    public Post getPost(Long pid) {
        return postRepository.findByPid(pid);
    }

    public List<Post> getPostAll() {
        return postRepository.findAll();
    }



    /* 글 쓰기 */
    public Long writePost(PostDTO postdto) {
        return writePost(postdto.getWriter(), postdto.getVideosrc(), postdto.getContent());
    }

    public Long writePost(User writer, String videosrc, String content) {
        Post post = Post.createPost(writer, videosrc, content);
        postRepository.save(post);
        return post.getPid();
    }


    /* 글 삭제 */



}
