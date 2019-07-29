package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.Follow;
import io.github.ryrie.vidflow.domain.Notification;
import io.github.ryrie.vidflow.domain.NotificationCategory;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.repository.NotificationRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class NotificationService {

    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void newPostNotify(UserPrincipal currentUser, Long postId) {

        User user = currentUser.getDomain();
        Set<Follow> followers = user.getFollowers();

        if(followers.size() == 0) return;

        Notification notification = new Notification();
        notification.setCategory(NotificationCategory.NEWPOST);
        notification.setFromuser(user);
        notification.setId(postId);

        for(Follow f : followers) {
            notification.setUser(f.getUser());
            notificationRepository.save(notification);
        }
    }
}
