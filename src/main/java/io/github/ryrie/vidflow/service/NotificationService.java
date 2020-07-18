package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.*;
import io.github.ryrie.vidflow.repository.NotificationRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@AllArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Transactional
    public void newPostNotify(UserPrincipal currentUser, Long postId) {

        User user = currentUser.getDomain();
        Set<Follow> followers = user.getFollowers();

        if(followers.size() == 0) return;

        Notification notification = new Notification();
        notification.setCategory(NotificationCategory.NEWPOST);
        notification.setFromuser(user);
        notification.setLink("/posts/" + postId);

        for(Follow f : followers) {
            notification.setUser(f.getUser());
            notificationRepository.save(notification);
        }
    }

    public void likePostNotify(UserPrincipal currentUser, Post post) {
        User user = currentUser.getDomain();

        Notification notification = new Notification();
        notification.setCategory(NotificationCategory.LIKE);
        notification.setFromuser(user);
        notification.setUser(post.getWriter());
        notification.setLink("/user/"+user.getId());
        notificationRepository.save(notification);
    }

    public void followNotify(UserPrincipal currentUser, User follower) {
        User user = currentUser.getDomain();
        Notification notification = new Notification();
        notification.setCategory(NotificationCategory.FOLLOW);
        notification.setFromuser(user);
        notification.setUser(follower);
        notification.setLink("/user/"+ user.getId());
        notificationRepository.save(notification);
    }

    public void commentNotify(UserPrincipal currentUser, Comment comment) {
        Notification notification = new Notification();
        notification.setCategory(NotificationCategory.COMMENT);
        notification.setFromuser(currentUser.getDomain());
        notification.setUser(comment.getPost().getWriter());
        notification.setLink("/posts/"+comment.getPost().getId());
        notificationRepository.save(notification);

    }
}
