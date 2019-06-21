package io.github.ryrie.vidflow.config;

import io.github.ryrie.vidflow.websocket.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.HashMap;
import java.util.Map;

/**
 * CORS(Cross-origin Resource Sharing) 설정을 한다.
 *
 */

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE", "")
                .maxAge(MAX_AGE_SECS);

    }

    @Bean
    public Map<Long, Client> client() {
        return new HashMap<>();
    }
}
