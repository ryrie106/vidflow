package io.github.ryrie.vidflow.repository;

import io.github.ryrie.vidflow.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

}
