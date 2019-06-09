package io.github.ryrie.vidflow.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID")
    private User writer;

//    @OneToMany(mappedBy = "post")
//    private List<Comment> comments = new ArrayList<>();

    private String videosrc;

    private String content;

    private Instant regdate;

    private Instant upadteddate;
}
