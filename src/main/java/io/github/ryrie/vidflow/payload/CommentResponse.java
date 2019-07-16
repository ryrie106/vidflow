package io.github.ryrie.vidflow.payload;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class CommentResponse {

    private Long id;
    private String writername;
    private Long writerid;
    private String content;
    private Instant regdate;
    private Instant updateddate;

}
