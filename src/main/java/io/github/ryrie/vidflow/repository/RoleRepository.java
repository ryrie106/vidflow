package io.github.ryrie.vidflow.repository;

import io.github.ryrie.vidflow.domain.Role;
import io.github.ryrie.vidflow.domain.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleName);
}
