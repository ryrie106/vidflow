package io.github.ryrie.vidflow.domain;

import lombok.Data;
import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Data
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postno;

    private String writer;

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

    public static Post createPost(String writer, String videosrc, String content) {
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
