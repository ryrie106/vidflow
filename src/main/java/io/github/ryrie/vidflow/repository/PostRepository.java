package io.github.ryrie.vidflow.repository;

import io.github.ryrie.vidflow.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    @Query("SELECT coalesce(max(p.id), 0) FROM Post p")
    Long getMaxId();

    List<Post> findTop5ByIdLessThanEqualOrderByIdDesc(Long id);

    List<Post> findByContentContaining(String content);

}
