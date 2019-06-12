package io.github.ryrie.vidflow.util;

import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.payload.PostResponse;

public class Mapper {
    public static PostResponse mapPostToPostResponse(Post post) {

        PostResponse postResponse = new PostResponse();
        postResponse.setWriter(post.getWriter());
        postResponse.setVideosrc(post.getVideosrc());
        postResponse.setContent(post.getContent());
        postResponse.setRegdate(post.getRegdate());
        postResponse.setUpdateddate(post.getUpadteddate());

        // TODO: Like와 Comment 기능 추가
        postResponse.setNum_comment(0L);
        postResponse.setNum_like(0L);

        return postResponse;
    }
}
