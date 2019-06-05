package io.github.ryrie.vidflow.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserDTO {
    private String email; // 여기서는 email 주소가 username
    private String password;
    private String name;
}