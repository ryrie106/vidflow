package io.github.ryrie.vidflow.util;

import io.github.ryrie.vidflow.domain.Comment;
import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.payload.CommentResponse;
import io.github.ryrie.vidflow.payload.PostResponse;

import java.util.List;

public class Mapper {
    public static PostResponse mapPostToPostResponse(Post post,
                                                     List<Long> likePostIds,
                                                     Long numComment,
                                                     Long numLike,
                                                     User postWriter) {

        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setWritername(postWriter.getName());
        postResponse.setWriterid(postWriter.getId());
        postResponse.setVideosrc(post.getVideosrc());
        postResponse.setContent(post.getContent());
        postResponse.setRegdate(post.getRegdate());
        postResponse.setUpdateddate(post.getUpadteddate());

        postResponse.setIsliked(likePostIds.contains(post.getId()));
        postResponse.setNum_comment(numComment);
        postResponse.setNum_like(numLike);

        return postResponse;
    }

    public static CommentResponse mapCommentToCommentResponse(Comment comment, User postWriter) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setWritername(postWriter.getName());
        commentResponse.setWriterid(postWriter.getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setRegdate(comment.getRegdate());
        commentResponse.setUpdateddate(comment.getUpdatedate());

        return commentResponse;
    }
}
