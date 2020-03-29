package io.github.ryrie.vidflow.conroller;

import io.github.ryrie.vidflow.controller.PostController;
import io.github.ryrie.vidflow.security.CustomUserDetailsService;
import io.github.ryrie.vidflow.service.NotificationService;
import io.github.ryrie.vidflow.service.PostService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

//@WebMvcTest(PostController.class)
public class PostControllerTest {

//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private PostService postService;
//
//    @MockBean
//    private NotificationService notificationService;
//
//    @MockBean
//    private CustomUserDetailsService customUserDetailsService;
//
//    @Test
//    public void testGetPosts() {
//
//        this.mvc.perform(get("/posts").)
//    }
}
