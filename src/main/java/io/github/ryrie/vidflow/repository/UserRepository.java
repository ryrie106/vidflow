package io.github.ryrie.vidflow.repository;

import io.github.ryrie.vidflow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    Boolean existsByName(String name);

    List<User> findByNameContaining(String name);

    List<User> findByIdIn(List<Long> ids);

}
