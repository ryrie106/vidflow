package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.domain.AuthenticationRequest;
import io.github.ryrie.vidflow.domain.AuthenticationToken;
import io.github.ryrie.vidflow.domain.User;
import io.github.ryrie.vidflow.domain.UserDTO;
import io.github.ryrie.vidflow.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RequestMapping("/user")
@RestController
public class UserController {

    @Setter(onMethod_ = @Autowired)
    AuthenticationManager authenticationManager;

    @Setter(onMethod_ = @Autowired)
    UserService userService;

    @PostMapping("/login")
    public AuthenticationToken login(
            @RequestBody AuthenticationRequest authenticationRequest,
            HttpSession session) {
        String username = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token); // 스프링 시큐리티에 설정한 인증이 적용됨
        SecurityContextHolder.getContext().setAuthentication(authentication); // context에 인증 결과를 set해준다.
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());

        User user = userService.getUser(username);
        return new AuthenticationToken(user.getEmail(), user.getAuthorities(), session.getId());
    }

    @PostMapping(value = "/create",
            consumes = "application/json",
            produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<String> createUser(@RequestBody UserDTO dto) {
        User user = User.createUser(dto);
        // TODO: Password encode는 frontend에서 해 주어야 한다.
        user.setPassword(User.PASSWORD_ENCODER.encode(user.getPassword()));
        Long uid = userService.createUser(user);
        return uid > 0 ? new ResponseEntity<>("success", HttpStatus.OK) : new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
