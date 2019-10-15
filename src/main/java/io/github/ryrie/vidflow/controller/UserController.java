package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.payload.*;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.service.NotificationService;
import io.github.ryrie.vidflow.service.PostService;
import io.github.ryrie.vidflow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    private UserService userService;
    private PostService postService;
    private NotificationService notificationService;

    public UserController(UserService userService, PostService postService, NotificationService notificationService) {
        this.userService = userService;
        this.postService = postService;
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody SignUpRequest signUpRequest) {
        URI location = userService.createUser(signUpRequest);
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @GetMapping
    public List<QueryUserResponse> queryUsersName(@RequestParam("name") String name) {
        return userService.queryUserName(name);
    }

    @GetMapping("/me")
    public UserSummary getCurrentUser(@AuthenticationPrincipal UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
    }

    @GetMapping("/{userId}")
    public UserInfoResponse getUserInfo(@PathVariable Long userId) {
        return userService.getUserInfo(userId);
    }

    @GetMapping("/{userId}/notification")
    public List<NotificationResponse> getNotifications(@PathVariable Long userId) {
        return userService.getNotifications(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> editUser(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long userId) {
        return null;
    }

    @GetMapping("/{userId}/follow")
    public boolean isFollowing(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long userId) {
        return userService.isFollowing(currentUser, userId);
    }

    @PostMapping("/{userId}/follow")
    @Transactional
    public ResponseEntity<?> followUser(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long userId) {
        User follower = userService.followUser(currentUser, userId);
        notificationService.followNotify(currentUser, follower);
        return ResponseEntity.ok().body(new ApiResponse(true, "Follow user Successfully"));
    }

    @DeleteMapping("/{userId}/follow")
    public ResponseEntity<?> unfollowUser(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long userId) {
        userService.unfollowUser(currentUser, userId);
        return ResponseEntity.ok().body(new ApiResponse(true, "unFollow user Successfully"));
    }

    @GetMapping("/{userId}/posts")
    public List<QueryPostsResponse> getUserPosts(@PathVariable Long userId) {
        return postService.getUserPosts(userId);
    }

    @GetMapping("/{userId}/likes")
    public List<QueryPostsResponse> getUserLikes(@PathVariable Long userId) {
        return postService.getUserLikes(userId);
    }

//    @GetMapping("/checkNameAvailability")
//    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "name") String name) {
//        Boolean isAvailable = !userRepository.existsByName(name);
//        return new UserIdentityAvailability(isAvailable);
//    }
//
//    @GetMapping("/checkEmailAvailability")
//    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
//        Boolean isAvailable = !userRepository.existsByEmail(email);
//        return new UserIdentityAvailability(isAvailable);
//    }

}
