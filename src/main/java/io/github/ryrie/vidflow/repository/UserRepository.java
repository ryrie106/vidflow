package io.github.ryrie.vidflow.repository;

import io.github.ryrie.vidflow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUid(Long uid) ;

    User findByEmail(String email);
}
