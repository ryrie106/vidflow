package io.github.ryrie.vidflow.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserPostsResponse {

    private Long postId;
    private String thumbnailSrc;
}
