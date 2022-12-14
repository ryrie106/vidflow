package io.github.ryrie.vidflow.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UserSummary {
    private Long id;
    private String email;
    private String name;
}
