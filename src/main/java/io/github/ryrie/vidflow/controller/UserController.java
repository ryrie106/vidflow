package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.domain.Notification;
import io.github.ryrie.vidflow.payload.*;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.security.CurrentUser;
import io.github.ryrie.vidflow.security.JwtTokenProvider;
import io.github.ryrie.vidflow.security.UserPrincipal;
import io.github.ryrie.vidflow.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private UserRepository userRepository;
    private JwtTokenProvider tokenProvider;

    public UserController(AuthenticationManager authenticationManager, UserService userService,
                          UserRepository userRepository, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new ApiResponse(true, jwt));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody SignUpRequest signUpRequest) {
        URI location = userService.createUser(signUpRequest);
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }

    @GetMapping("/info/{userId}")
    public UserInfoResponse getUserInfo(@PathVariable Long userId) {
        return userService.getUserInfo(userId);
    }

    @GetMapping("/notification/{userId}")
    public List<NotificationResponse> getNotifications(@PathVariable Long userId) {
        return userService.getNotifications(userId);
    }

    @GetMapping("/me")
    public UserSummary getCurrnetUser(@CurrentUser UserPrincipal currentUser) {
        return new UserSummary(currentUser.getId(), currentUser.getUsername(), currentUser.getName());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> editUser(@CurrentUser UserPrincipal currentUser) {
        return null;
    }

    @PostMapping("/follow/{userId}")
    public ResponseEntity<?> followUser(@CurrentUser UserPrincipal currentUser, @PathVariable Long userId) {
        userService.followUser(currentUser, userId);
        return ResponseEntity.ok().body(new ApiResponse(true, "Follow user Successfully"));
    }

    @DeleteMapping("/follow/{userId}")
    public ResponseEntity<?> unfollowUser(@CurrentUser UserPrincipal currentUser, @PathVariable Long userId) {
        userService.unfollowUser(currentUser, userId);
        return ResponseEntity.ok().body(new ApiResponse(true, "unFollow user Successfully"));
    }

    @GetMapping("/checkNameAvailability")
    public UserIdentityAvailability checkUsernameAvailability(@RequestParam(value = "name") String name) {
        Boolean isAvailable = !userRepository.existsByName(name);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/checkEmailAvailability")
    public UserIdentityAvailability checkEmailAvailability(@RequestParam(value = "email") String email) {
        Boolean isAvailable = !userRepository.existsByEmail(email);
        return new UserIdentityAvailability(isAvailable);
    }

    @GetMapping("/query")
    public List<QueryUserResponse> queryUsersName(@RequestParam("name") String name) {
        return userService.queryUserName(name);
    }

    @GetMapping("/following")
    public boolean isFollowing(@RequestParam("from") Long from, @RequestParam("to") Long to) {
        return userService.isFollowing(from, to);
    }
}
