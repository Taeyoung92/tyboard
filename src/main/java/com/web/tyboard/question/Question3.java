package com.web.tyboard.question;

import com.web.tyboard.answer.Answer3;
import com.web.tyboard.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
public class Question3 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 200)
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createDate;

    @OneToMany(mappedBy = "question3", cascade = CascadeType.REMOVE)
    private List<Answer3> answer3List;

    @ManyToOne
    private User author;

    private LocalDateTime modifyDate;

    @ManyToMany
    Set<User> voter;
}
