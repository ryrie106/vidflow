package io.github.ryrie.vidflow.service;

import lombok.Setter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CommentServiceTest {

    @Setter(onMethod_ = @Autowired)
    private CommentService commentService;


    @Before
    public void init() {

    }

    @Test
    public void testCreate() {

    }

}
