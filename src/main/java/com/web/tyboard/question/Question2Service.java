package com.web.tyboard.question;

import com.web.tyboard.DataNotFoundException;
import com.web.tyboard.answer.Answer2;
import com.web.tyboard.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class Question2Service {
    private final Question2Repository question2Repository;

    private Specification<Question2> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 2L;
            @Override
            public Predicate toPredicate(Root<Question2> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question2, User> u1 = q.join("author", JoinType.LEFT);
                Join<Question2, Answer2> a = q.join("answer2List", JoinType.LEFT);
                Join<Answer2, User> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public List<Question2> getList() {
        return this.question2Repository.findAll();
    }

    public Question2 getQuestion2(Integer id) {
        Optional<Question2> question2 = this.question2Repository.findById(id);
        if (question2.isPresent()) {
            return question2.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public Page<Question2> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 20, Sort.by(sorts));
        Specification<Question2> spec = search(kw);
        return this.question2Repository.findAll(spec, pageable);
    }

    public void create(String subject, String content, User user) {
        Question2 q = new Question2();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.question2Repository.save(q);
    }

    public void modify(Question2 question2, String subject, String content) {
        question2.setSubject(subject);
        question2.setContent(content);
        question2.setModifyDate(LocalDateTime.now());
        this.question2Repository.save(question2);
    }

    public void delete(Question2 question2) {
        this.question2Repository.delete(question2);
    }

    public void vote(Question2 question2, User siteUser) {
        question2.getVoter().add(siteUser);
        this.question2Repository.save(question2);
    }
}
