package io.github.ryrie.vidflow.payload;

import io.github.ryrie.vidflow.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequest {

    private String content;
}
