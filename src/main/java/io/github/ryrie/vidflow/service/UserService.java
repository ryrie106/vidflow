package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(email); // email이 username이다.
    }

    /* 유저정보 읽기 */
    public User getUser(Long uid) {
        return userRepository.findByUid(uid);
    }
    // TODO: email로 uid를 찾는 방법은?
    public User getUser(String email) { return userRepository.findByEmail(email);}

    /* 회원 가입 */
    public Long createUser(User user) {
        this.userRepository.save(user);
        return user.getUid();
    }

    /* 회원 탈퇴 */
    public void deleteUser() {

    }


}
