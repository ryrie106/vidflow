package io.github.ryrie.vidflow.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class PostResponse {
    private Long id;

    private String writername;
    private Long writerid;
    private String videosrc;
    private String content;
    private Boolean isfollowed;
    private Boolean isliked;
    private Instant regdate;
    private Instant updateddate;

    private Long num_like;
    private Long num_comment;
}
