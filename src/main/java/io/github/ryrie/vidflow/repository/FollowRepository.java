package io.github.ryrie.vidflow.repository;

import io.github.ryrie.vidflow.domain.Follow;
import io.github.ryrie.vidflow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByUserAndFollower(User User, User Follower);
}
