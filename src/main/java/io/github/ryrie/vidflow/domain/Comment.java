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

@Getter
@Setter
@Entity
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User writer;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    private String content;

    @CreatedDate
    private Instant regdate;

    @LastModifiedDate
    private Instant updatedate;

}
