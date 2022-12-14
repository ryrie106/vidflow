package io.github.ryrie.vidflow.domain;

import lombok.Data;

import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "email"
        })
})
@NoArgsConstructor
public class User {

    @Id @GeneratedValue
    private Long id;

    private String email; // 이메일이 로그인 아이디.

    private String name;

    private String password;

    private String introduction = "";

    private Long num_liked = 0L;

    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "writer", fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    // 자신이 팔로우 하고 있는 유저를 매핑
    // 사용 예: user.getFollowing().getFollower();
    @OneToMany(mappedBy = "user", fetch= FetchType.EAGER)
    private Set<Follow> following = new HashSet<>();

    // 자신을 팔로우 하고 있는 유저를 매핑
    // 사용 예 : user.getFollowers().getUser();
    @OneToMany(mappedBy = "follower", fetch = FetchType.EAGER)
    private Set<Follow> followers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Notification> notifications = new ArrayList<>();

    @OneToMany(mappedBy = "fromuser", fetch = FetchType.LAZY)
    private List<Notification> notifications_fromuser = new ArrayList<>();

    public User(String email, String name, String password) {
        this.name = name;
        this.email = email;
        this.password = password;

    }
}
