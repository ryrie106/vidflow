package io.github.ryrie.vidflow.security;

/**
 * https://github.com/callicoder/spring-security-react-ant-design-polls-app/blob/master/polling-app-server/src/main/java/com/example/polls/security/CurrentUser.java
 */

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.lang.annotation.*;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
public @interface CurrentUser {
}
