package io.github.ryrie.vidflow.util;

import io.github.ryrie.vidflow.domain.*;
import io.github.ryrie.vidflow.payload.CommentResponse;
import io.github.ryrie.vidflow.payload.PostResponse;

public class Mapper {
    public static PostResponse mapPostToPostResponse(Post post, User currentUser) {

        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        User writer = post.getWriter();
        // TODO: writer가 null이 될수가 있는가?
        if(writer != null) {
            postResponse.setWritername(writer.getName());
            postResponse.setWriterid(writer.getId());
        } else {
            // 탈퇴된 회원의 경우.
            postResponse.setWritername("deleted");
            postResponse.setWriterid(0L);
        }
        postResponse.setVideosrc(post.getVideosrc());
        postResponse.setContent(post.getContent());
        postResponse.setUpdateddate(post.getUpadteddate());
        postResponse.setRegdate(post.getRegdate());
        postResponse.setIsliked(false);
        postResponse.setIsfollowed(false);

        if(currentUser != null) {
            // 로그인한 경우 좋아요 여부 확인
            for (Like like : post.getLikes()) {
                if(currentUser.getId().equals(like.getUser().getId())) {
                    postResponse.setIsliked(true);
                    break;
                }
            }
        }

        postResponse.setNum_comment((long) post.getComments().size());
        postResponse.setNum_like((long) post.getLikes().size());

        return postResponse;
    }

    public static CommentResponse mapCommentToCommentResponse(Comment comment) {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setId(comment.getId());
        commentResponse.setContent(comment.getContent());
        commentResponse.setRegdate(comment.getRegdate());
        commentResponse.setUpdateddate(comment.getUpdatedate());

        User writer = comment.getWriter();
        if(writer != null) {
            commentResponse.setWritername(writer.getName());
            commentResponse.setWriterid(writer.getId());
        } else {
            // 탈퇴한 회원인 경우
            commentResponse.setWritername("deleted");
            commentResponse.setWriterid(0L);
        }
        return commentResponse;
    }
}
