package com.web.tyboard.answer;

import com.web.tyboard.question.Question3;
import com.web.tyboard.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Entity
public class Answer3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String content;

    @CreatedDate
    private LocalDateTime createDate;

    @ManyToOne
    private Question3 question3;

    @ManyToOne
    private User author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<User> voter;
}
