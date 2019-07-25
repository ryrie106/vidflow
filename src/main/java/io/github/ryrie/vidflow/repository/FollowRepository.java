package io.github.ryrie.vidflow.repository;

import io.github.ryrie.vidflow.domain.Follow;
import io.github.ryrie.vidflow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByUser(User user);

    List<Follow> findByFollower(User follower);

    Optional<Follow> findByUserAndFollower(User User, User Follower);
}
