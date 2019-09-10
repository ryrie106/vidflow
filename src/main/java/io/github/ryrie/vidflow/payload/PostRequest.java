package io.github.ryrie.vidflow.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {

    private String thumbnailSrc;
    private String videoSrc;
    private String content;
}
