package io.github.ryrie.vidflow.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post {

    /**
     * 글 번호 id Integer PK
     *     작성자 writer varchar
     *     작성시간
     *     좋아요 수 like Integer
     *     내용 content varchar
     *     영상 경로 videosrc varchar
     */

    @Id @GeneratedValue
    private Long postno;

    private String writer;

    private String videosrc;

    private String content;

    private int numcomment;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regdate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date upadtedate;



}
