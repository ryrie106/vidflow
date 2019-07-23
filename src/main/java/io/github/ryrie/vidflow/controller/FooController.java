package io.github.ryrie.vidflow.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class FooController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(@Payload Message message) {
        log.info(message.getMsg());
        return "NONONONONONO";
    }
}


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class Message {
    String msg;
}
