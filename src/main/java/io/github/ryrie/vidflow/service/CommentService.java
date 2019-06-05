package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Comment;
import io.github.ryrie.vidflow.domain.CommentDTO;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.repository.CommentRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private UserRepository userRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    /* 댓글 읽기 */
    public List<Comment> getComments(Post post) {
        return commentRepository.findByPost(post);
    }


    /* 댓글 쓰기 */
    public Long writeComment(CommentDTO commentdto) {

        Post post = postRepository.findByPid(commentdto.getPid());
        User user = userRepository.findByUid(commentdto.getMid());

        Comment comment = Comment.createComment(post, user, commentdto.getContent());
        commentRepository.save(comment);
        return comment.getCid();
    }

    /* 댓글 수정 */


    /* 댓글 삭제 */


}
