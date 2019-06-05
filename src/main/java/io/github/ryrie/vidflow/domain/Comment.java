package io.github.ryrie.vidflow.domain;

import lombok.Data;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Data
@Entity
public class Comment {

    @Id @GeneratedValue
    @Column(name = "COMMENT_ID")
    private Long cid;

    @ManyToOne
    @JoinColumn(name = "POST_ID")
    private Post post;

//    @ManyToOne
//    @JoinColumn(name = "MEMBER_ID")
//    private User writer;

    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedate;

    public Comment() {}

    public static Comment createComment(Post post, User user, String content) {
        Comment comment = new Comment();
        comment.setPost(post);
//        comment.setWriter(user);
        comment.setContent(content);
        comment.setRegdate(Date.from(Instant.now()));
        comment.setUpdatedate(Date.from(Instant.now()));

        return comment;
    }
}
