package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.domain.*;
import io.github.ryrie.vidflow.exception.AppException;
import io.github.ryrie.vidflow.payload.*;
import io.github.ryrie.vidflow.repository.FollowRepository;
import io.github.ryrie.vidflow.repository.RoleRepository;
import io.github.ryrie.vidflow.repository.UserRepository;
import io.github.ryrie.vidflow.security.CurrentUser;
import io.github.ryrie.vidflow.security.JwtTokenProvider;
import io.github.ryrie.vidflow.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@RequestMapping("/user")
@RestController
public class UserController {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private FollowRepository followRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;

    @Autowired
    public UserController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          RoleRepository roleRepository, FollowRepository followRepository,
                          PasswordEncoder passwordEncoder, JwtTokenProvider tokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.followRepository = followRepository;
        this.passwordEncoder = passwordEncoder;
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
//        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
        return ResponseEntity.ok(new ApiResponse(true, jwt));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        User user = new User(signUpRequest.getEmail(),
                signUpRequest.getName(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));
//        user.setPassword(user.getPassword());

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/{email}")
                .buildAndExpand(result.getEmail()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
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
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new AppException("findbyid error during followUser"));
        User follower = userRepository.findById(userId).orElseThrow(() -> new AppException("findbyid error during followUser"));
        Follow follow = new Follow();
        follow.setUser(user);
        follow.setFollower(follower);
        followRepository.save(follow);
        return ResponseEntity.ok().body(new ApiResponse(true, "Follow user Successfully"));
    }

    @DeleteMapping("/follow/{userId}")
    public ResponseEntity<?> unfollowUser(@CurrentUser UserPrincipal currentUser, @PathVariable Long userId) {
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new AppException("findbyid error during unfollowUser"));
        User follower = userRepository.findById(userId).orElseThrow(() -> new AppException("findbyid error during unfollowUser"));
        Follow follow = followRepository.findByUserAndFollower(user, follower).orElseThrow(() -> new AppException("findbyuserAndFollow error during unfollowUser"));
        followRepository.delete(follow);
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
}
