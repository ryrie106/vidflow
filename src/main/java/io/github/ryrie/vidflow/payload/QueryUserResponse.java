package io.github.ryrie.vidflow.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class QueryUserResponse {

    private Long id;
    private String name;
    private String thumbnailSrc;
    private String introduction;
}
