package io.github.ryrie.vidflow.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.Instant;

@Data
@Entity
@NoArgsConstructor
public class Comment {

    @Id @GeneratedValue
    private Long id;

    @CreatedBy
    private Long writer;

    private Long post;

    private String content;

    @CreatedDate
    private Instant regdate;

    @LastModifiedDate
    private Instant updatedate;

}
