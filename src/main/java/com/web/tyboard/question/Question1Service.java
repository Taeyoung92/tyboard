package com.web.tyboard.question;

import com.web.tyboard.DataNotFoundException;
import com.web.tyboard.answer.Answer1;
import com.web.tyboard.user.User;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Question1Service {
    private final Question1Repository question1Repository;

    private Specification<Question1> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 1L;
            @Override
            public Predicate toPredicate(Root<Question1> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question1, User> u1 = q.join("author", JoinType.LEFT);
                Join<Question1, Answer1> a = q.join("answer1List", JoinType.LEFT);
                Join<Answer1, User> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public List<Question1> getList() {
        return this.question1Repository.findAll();
    }

    public Question1 getQuestion1(Integer id) {
        Optional<Question1> question1 = this.question1Repository.findById(id);
        if (question1.isPresent()) {
            return question1.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public Page<Question1> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Specification<Question1> spec = search(kw);
        return this.question1Repository.findAll(spec, pageable);
    }

    public void create(String subject, String content, User user) {
        Question1 q = new Question1();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.question1Repository.save(q);
    }

    public void modify(Question1 question1, String subject, String content) {
        question1.setSubject(subject);
        question1.setContent(content);
        question1.setModifyDate(LocalDateTime.now());
        this.question1Repository.save(question1);
    }

    public void delete(Question1 question1) {
        this.question1Repository.delete(question1);
    }

    public void vote(Question1 question1, User user) {
        question1.getVoter().add(user);
        this.question1Repository.save(question1);
    }
}
