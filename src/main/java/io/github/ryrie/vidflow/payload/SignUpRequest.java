package io.github.ryrie.vidflow.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequest {

    private String email;
    private String name;
    private String password;
}
