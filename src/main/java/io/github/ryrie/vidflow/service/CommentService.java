package io.github.ryrie.vidflow.service;


import io.github.ryrie.vidflow.domain.Comment;
import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.exception.AppException;
import io.github.ryrie.vidflow.payload.CommentRequest;
import io.github.ryrie.vidflow.payload.CommentResponse;
import io.github.ryrie.vidflow.repository.CommentRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public List<CommentResponse> getCommentsByPostId(UserPrincipal currentUser, Long postId) {
        List<Comment> comments = commentRepository.findByPost(postId);

        Map<Long, User> writerMap = getCommentWriterMap(comments);
        return comments.stream()
                .map(comment ->
                        Mapper.mapCommentToCommentResponse(comment,
                                writerMap.get(comment.getWriter())))
                .collect(Collectors.toList());
    }

    public Comment createComment(CommentRequest commentRequest, Long postId) {
        Comment comment = new Comment();

        comment.setContent(commentRequest.getContent());
        comment.setPost(postId);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new AppException("Get Comment Error!"));
        commentRepository.delete(comment);
    }

    private Map<Long, User> getCommentWriterMap(List<Comment> comments) {
        List<Long> writerIds = comments.stream()
                .map(Comment::getWriter)
                .distinct()
                .collect(Collectors.toList());

        List<User> writers = userRepository.findByIdIn(writerIds);
        return writers.stream().collect(Collectors.toMap(User::getId, Function.identity()));
    }


}
