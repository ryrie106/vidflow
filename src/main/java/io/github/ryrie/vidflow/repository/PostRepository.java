package io.github.ryrie.vidflow.repository;

import io.github.ryrie.vidflow.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByPid(Long pid);
}
