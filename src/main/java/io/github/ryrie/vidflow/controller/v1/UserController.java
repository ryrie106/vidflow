package io.github.ryrie.vidflow.controller.v1;

import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.payload.*;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.service.NotificationService;
import io.github.ryrie.vidflow.service.PostService;
import io.github.ryrie.vidflow.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@AllArgsConstructor
@RequestMapping("/v1/users")
@RestController
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final NotificationService notificationService;

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
        if(currentUser == null) {
            return new UserSummary(0L, "guest", "guest");
        }
        else return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
    }

    @GetMapping("/{userId}")
    public UserInfoResponse getUserInfo(@PathVariable Long userId) {
        return userService.getUserInfo(userId);
    }

    // TODO: userId 안받도록 수정하기
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
}
