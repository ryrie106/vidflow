package io.github.ryrie.vidflow.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FileInfoResponse {
    private String url;
    private String fileId;
}
