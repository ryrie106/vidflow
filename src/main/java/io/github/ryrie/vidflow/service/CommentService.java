package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Comment;
import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.exception.AppException;
import io.github.ryrie.vidflow.payload.CommentRequest;
import io.github.ryrie.vidflow.payload.CommentResponse;
import io.github.ryrie.vidflow.repository.CommentRepository;
import io.github.ryrie.vidflow.repository.PostRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public List<CommentResponse> getCommentsByPostId(UserPrincipal currentUser, Long postId) {
        // TODO: 애매한 Optional
        Optional<Post> post = postRepository.findById(postId);
        List<Comment> comments = commentRepository.findByPost(post.get());

        return comments.stream().map(Mapper::mapCommentToCommentResponse).collect(Collectors.toList());
    }

    public Comment createComment(CommentRequest commentRequest, Long postId) {
        Comment comment = new Comment();
        // 존재하지 않는 Post에 댓글을 달 수 없다.
        Post post = postRepository.findById(postId).orElseThrow(() -> new AppException("findById in createComment"));

        comment.setContent(commentRequest.getContent());
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new AppException("Get Comment Error!"));
        commentRepository.delete(comment);
    }
}
