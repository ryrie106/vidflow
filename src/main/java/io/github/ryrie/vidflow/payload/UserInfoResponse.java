package io.github.ryrie.vidflow.payload;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserInfoResponse {

    private String name;
    private String introduction;

    private Long numLikes;
    private Long numFollowing;
    private Long numFollower;
}
