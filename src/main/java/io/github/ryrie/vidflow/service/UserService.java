package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.*;
import io.github.ryrie.vidflow.exception.AppException;
import io.github.ryrie.vidflow.payload.*;
import io.github.ryrie.vidflow.repository.FollowRepository;
import io.github.ryrie.vidflow.repository.RoleRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService {

    private final RoleRepository roleRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public URI createUser(SignUpRequest signUpRequest) {
//        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
//            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
//                    HttpStatus.BAD_REQUEST);
//        }
        User user = new User(signUpRequest.getEmail(),
                signUpRequest.getName(), signUpRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
        User result = userRepository.save(user);

        return ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/{email}")
                .buildAndExpand(result.getEmail()).toUri();
    }

    public UserInfoResponse getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElse(new User());
        UserInfoResponse userInfoResponse = new UserInfoResponse();
        userInfoResponse.setName(user.getName());
        userInfoResponse.setIntroduction(user.getIntroduction());

        userInfoResponse.setNumLikes(user.getNum_liked());
        userInfoResponse.setNumFollowing((long) user.getFollowing().size());
        userInfoResponse.setNumFollower((long) user.getFollowers().size());

        return userInfoResponse;
    }

    public User followUser(UserPrincipal currentUser, Long userId) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new AppException("findbyid error during followUser"));
        User follower = userRepository.findById(userId).orElseThrow(() -> new AppException("findbyid error during followUser"));
        Follow follow = new Follow();
        follow.setUser(user);
        follow.setFollower(follower);
        followRepository.save(follow);
        return follower;
    }

    public void unfollowUser(UserPrincipal currentUser, Long userId) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new AppException("findbyid error during unfollowUser"));
        User follower = userRepository.findById(userId).orElseThrow(() -> new AppException("findbyid error during unfollowUser"));
        Follow follow = followRepository.findByUserAndFollower(user, follower).orElseThrow(() -> new AppException("findbyuserAndFollow error during unfollowUser"));
        followRepository.delete(follow);
    }

    public boolean isFollowing(UserPrincipal currentUser, Long follower) {
        User user = userRepository.findById(currentUser.getId()).orElse(new User());
        for(Follow f : user.getFollowing()) {
            if(f.getFollower().getId().equals(follower)) {
                return true;
            }
        }
        return false;
    }

    public List<QueryUserResponse> queryUserName(String name) {
        List<User> users = userRepository.findByNameContaining(name);
        return users.stream().map(user -> {
            QueryUserResponse response = new QueryUserResponse();
            response.setId(user.getId());
            response.setName(user.getName());
            response.setThumbnailSrc("");
            response.setIntroduction(user.getIntroduction());
            return response;
        }).collect(Collectors.toList());
    }

    public List<NotificationResponse> getNotifications(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException("findByid during getNotifications"));
        List<Notification> notifications = user.getNotifications();
        // ????????? ????????? ??? ?????? ?????????
        Collections.reverse(notifications);

        return notifications.stream().map(notification -> {
            NotificationResponse response = new NotificationResponse();
            response.setCategory(notification.getCategory().toString());
            response.setLink(notification.getLink());
            response.setFromname(notification.getFromuser().getName());
            response.setId(notification.getId());
            return response;
        }).collect(Collectors.toList());
    }
}
