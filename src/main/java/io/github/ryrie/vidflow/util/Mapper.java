package io.github.ryrie.vidflow.util;

import io.github.ryrie.vidflow.domain.Comment;
import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.payload.CommentResponse;
import io.github.ryrie.vidflow.payload.PostResponse;

public class Mapper {
    public static PostResponse mapPostToPostResponse(Post post, User writer) {

        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setWritername(writer.getName());
        postResponse.setVideosrc(post.getVideosrc());
        postResponse.setContent(post.getContent());
        postResponse.setRegdate(post.getRegdate());
        postResponse.setUpdateddate(post.getUpadteddate());

        // TODO: Like와 Comment 기능 추가
        postResponse.setNum_comment(0L);
        postResponse.setNum_like(0L);

        return postResponse;
    }

    public static CommentResponse mapCommentToCommentResponse(Comment comment, User writer) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setWritername(writer.getName());
        commentResponse.setContent(comment.getContent());
        commentResponse.setRegdate(comment.getRegdate());
        commentResponse.setUpdateddate(comment.getUpdatedate());

        return commentResponse;
    }
}
