package io.github.ryrie.vidflow.payload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class PostRequest {

    private String thumbnailsrc;
    private String videoSrc;
    private String content;
}
