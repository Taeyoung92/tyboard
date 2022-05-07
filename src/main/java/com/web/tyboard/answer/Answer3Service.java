package com.web.tyboard.answer;

import com.web.tyboard.DataNotFoundException;
import com.web.tyboard.question.Question3;
import com.web.tyboard.user.SiteUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class Answer3Service {
    private final Answer3Repository answer3Repository;


    public Answer3 create(Question3 question3, String content, SiteUser author) {
        Answer3 answer3 = new Answer3();
        answer3.setContent(content);
        answer3.setCreateDate(LocalDateTime.now());
        answer3.setQuestion3(question3);
        answer3.setAuthor(author);
        this.answer3Repository.save(answer3);
        return answer3;
    }

    public Answer3 getAnswer3(Integer id) {
        Optional<Answer3> answer3 = this.answer3Repository.findById(id);
        if (answer3.isPresent()) {
            return answer3.get();
        } else {
            throw new DataNotFoundException("answer not found");
        }
    }

    public void modify(Answer3 answer3, String content) {
        answer3.setContent(content);
        answer3.setModifyDate(LocalDateTime.now());
        this.answer3Repository.save(answer3);
    }

    public void delete(Answer3 answer3) {
        this.answer3Repository.delete(answer3);
    }

    public void vote(Answer3 answer3, SiteUser siteUser) {
        answer3.getVoter().add(siteUser);
        this.answer3Repository.save(answer3);
    }
}
