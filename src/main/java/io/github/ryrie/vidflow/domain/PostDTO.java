package io.github.ryrie.vidflow.domain;

import lombok.Data;

@Data
public class PostDTO {

    private User writer;

    private String videosrc;

    private String content;
}
