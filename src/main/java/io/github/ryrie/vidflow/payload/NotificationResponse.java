package io.github.ryrie.vidflow.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationResponse {
    private Long id;
    private String link;
    private String category;
    private String username;
}
