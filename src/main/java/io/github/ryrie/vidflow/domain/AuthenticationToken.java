package io.github.ryrie.vidflow.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class AuthenticationToken {
    private String username;
    private Collection authorities;
    private String token;
}
