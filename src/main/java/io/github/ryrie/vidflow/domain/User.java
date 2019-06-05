package io.github.ryrie.vidflow.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class User implements UserDetails {

    public static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Id @GeneratedValue
    @Column(name = "USER_ID")
    private Long uid;

    private String email; // 여기서는 email 주소가 username

    private String password;

    private String name;

//    @OneToMany(mappedBy = "writer")
//    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "writer")
    private List<Post> posts = new ArrayList<>();

    private String[] authorities = {"USER"};// UserDetails

    private boolean isAccountNonExpired = true; // UserDetails

    private boolean isAccountNonLocked = true;// UserDetails

    private boolean isCredentialsNonExpired = true;// UserDetails

    private boolean isEnabled = true;// UserDetails

    public static User createUser(UserDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        return user;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String authority : this.authorities) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

}
