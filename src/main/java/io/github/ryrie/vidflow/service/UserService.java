package io.github.ryrie.vidflow.service;

import io.github.ryrie.vidflow.domain.*;
import io.github.ryrie.vidflow.exception.AppException;
import io.github.ryrie.vidflow.payload.*;
import io.github.ryrie.vidflow.repository.FollowRepository;
import io.github.ryrie.vidflow.repository.RoleRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private RoleRepository roleRepository;
    private FollowRepository followRepository;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @Autowired
    public UserService(RoleRepository roleRepository, FollowRepository followRepository,
                       PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.followRepository = followRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

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
        userInfoResponse.setNumFollowing((long) user.getFollowers().size());
        userInfoResponse.setNumFollower(user.getNum_follower());

        return userInfoResponse;
    }

    @Transactional
    public void followUser(UserPrincipal currentUser, Long userId) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new AppException("findbyid error during followUser"));
        User follower = userRepository.findById(userId).orElseThrow(() -> new AppException("findbyid error during followUser"));
        Follow follow = new Follow();
        follow.setUser(user);
        follow.setFollower(follower);
        followRepository.save(follow);
        // TODO: 경쟁조건이 있을까
        follower.setNum_follower(follower.getNum_follower()+1);
    }

    @Transactional
    public void unfollowUser(UserPrincipal currentUser, Long userId) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new AppException("findbyid error during unfollowUser"));
        User follower = userRepository.findById(userId).orElseThrow(() -> new AppException("findbyid error during unfollowUser"));
        Follow follow = followRepository.findByUserAndFollower(user, follower).orElseThrow(() -> new AppException("findbyuserAndFollow error during unfollowUser"));
        followRepository.delete(follow);
        // TODO: 경쟁조건이 있을까
        follower.setNum_follower(follower.getNum_follower()-1);
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

        return user.getNotifications().stream().map(notification -> {
            NotificationResponse response = new NotificationResponse();
            response.setCategory(notification.getCategory().toString());
            response.setLink(notification.getLink());
            response.setUsername(notification.getUser().getName());
            response.setId(notification.getId());
            return response;
        }).collect(Collectors.toList());
    }
}
