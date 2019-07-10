package io.github.ryrie.vidflow.controller;

import io.github.ryrie.vidflow.config.SecurityConfiguration;
import io.github.ryrie.vidflow.domain.Role;
import io.github.ryrie.vidflow.domain.RoleName;
import io.github.ryrie.vidflow.repository.RoleRepository;
import io.github.ryrie.vidflow.security.CustomUserDetailsService;
import io.github.ryrie.vidflow.security.JwtAuthenticationEntryPoint;
import io.github.ryrie.vidflow.security.JwtAuthenticationFilter;
import io.github.ryrie.vidflow.service.PostService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(PostController.class)
public class PostControllerTest {

    @Autowired
    private MockMvc mvc;

//    @MockBean
//    private RoleRepository roleRepository;

    @MockBean
    private PostService postService;

    @MockBean
    private SecurityConfiguration se;


//    @Before
//    public void before() {
//        roleRepository.save(new Role(1L, RoleName.ROLE_USER));
//    }

    @Test
    public void shouldCreatedPostGetByPostId() throws Exception {
        mvc.perform(get("/posts").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
    }

}
