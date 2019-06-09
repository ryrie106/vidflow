package io.github.ryrie.vidflow.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
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

    private Instant regdate;

    private Instant updatedate;

}
