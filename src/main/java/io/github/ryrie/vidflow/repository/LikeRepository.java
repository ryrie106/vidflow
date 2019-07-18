package io.github.ryrie.vidflow.repository;

import io.github.ryrie.vidflow.domain.Like;
import io.github.ryrie.vidflow.domain.Post;
import io.github.ryrie.vidflow.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByPostAndUser(Post post, User user);
    List<Like> findAllByUser(User user);
    Long countByPost(Post post);
}
