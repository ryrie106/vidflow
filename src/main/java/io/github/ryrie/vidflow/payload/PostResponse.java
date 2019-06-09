package io.github.ryrie.vidflow.payload;

import io.github.ryrie.vidflow.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PostResponse {
    private Long id;
    private User writer;
    private String videosrc;
    private String content;
    private Instant regdate;
    private Instant updateddate;

    private Long num_like;
    private Long num_comment;
}
