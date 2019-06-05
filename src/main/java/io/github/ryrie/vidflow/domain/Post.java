package io.github.ryrie.vidflow.domain;

import lombok.Data;
import javax.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long pid;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User writer;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    private String videosrc;

    private String content;

    private int numcomment;

    private int numlike;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date upadtedate;

    public Post() {
    }

    private void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public static Post createPost(User writer, String videosrc, String content) {
        Post post = new Post();
        post.setWriter(writer);
        post.setVideosrc(videosrc);
        post.setContent(content);
        post.setNumcomment(0);
        post.setNumlike(0);
        post.setRegdate(Date.from(Instant.now()));
        post.setUpadtedate(Date.from(Instant.now()));

        return post;
    }
}
