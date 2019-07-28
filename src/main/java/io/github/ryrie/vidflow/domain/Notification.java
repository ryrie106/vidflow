package io.github.ryrie.vidflow.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User fromuser;

    @Enumerated(EnumType.STRING)
    private NotificationCategory category;

    private String link;

}