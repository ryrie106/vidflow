package io.github.ryrie.vidflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class VidflowApplication {
    public static void main(String[] args) {
        SpringApplication.run(VidflowApplication.class, args);
    }

}
