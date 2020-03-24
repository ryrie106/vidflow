package io.github.ryrie.vidflow.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;

    private String videosrc;

    private String content;

    private String thumbnailsrc = "";

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private Set<Like> likes = new HashSet<>();

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    private Set<Comment> comments = new HashSet<>();

    @CreatedDate
    private Instant regdate;

    @LastModifiedDate
    private Instant upadteddate;
}
