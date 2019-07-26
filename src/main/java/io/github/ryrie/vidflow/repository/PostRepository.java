package io.github.ryrie.vidflow.repository;

import io.github.ryrie.vidflow.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT coalesce(max(p.id), 0) FROM Post p")
    Long getMaxId();

    Optional<Post> findById(Long id);

    Page<Post> findByIdLessThanEqual(Long id, Pageable pageable);

}
