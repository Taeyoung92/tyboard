package com.web.tyboard.answer;

import com.web.tyboard.DataNotFoundException;
import com.web.tyboard.question.Question1;
import com.web.tyboard.user.SiteUser;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Answer1Service {
    private final Answer1Repository answer1Repository;


    public Answer1 create(Question1 question1, String content, SiteUser author) {
        Answer1 answer1 = new Answer1();
        answer1.setContent(content);
        answer1.setCreateDate(LocalDateTime.now());
        answer1.setQuestion1(question1);
        answer1.setAuthor(author);
        this.answer1Repository.save(answer1);
        return answer1;
    }

    public Answer1 getAnswer1(Integer id) {
        Optional<Answer1> answer1 = this.answer1Repository.findById(id);
        if (answer1.isPresent()) {
            return answer1.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer1 answer1, String content) {
        answer1.setContent(content);
        answer1.setModifyDate(LocalDateTime.now());
        this.answer1Repository.save(answer1);
    }

    public void delete(Answer1 answer1) {
        this.answer1Repository.delete(answer1);
    }

    public void vote(Answer1 answer1, SiteUser siteUser) {
        answer1.getVoter().add(siteUser);
        this.answer1Repository.save(answer1);
    }
}
