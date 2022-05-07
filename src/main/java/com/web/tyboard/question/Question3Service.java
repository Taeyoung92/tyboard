package com.web.tyboard.question;

import com.web.tyboard.DataNotFoundException;
import com.web.tyboard.answer.Answer3;
import com.web.tyboard.user.SiteUser;
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
public class Question3Service {
    private final Question3Repository question3Repository;

    private Specification<Question3> search(String kw) {
        return new Specification<>() {
            private static final long serialVersionUID = 2L;
            @Override
            public Predicate toPredicate(Root<Question3> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.distinct(true);  // 중복을 제거
                Join<Question3, SiteUser> u1 = q.join("author", JoinType.LEFT);
                Join<Question3, Answer3> a = q.join("answer3List", JoinType.LEFT);
                Join<Answer3, SiteUser> u2 = a.join("author", JoinType.LEFT);
                return cb.or(cb.like(q.get("subject"), "%" + kw + "%"), // 제목
                        cb.like(q.get("content"), "%" + kw + "%"),      // 내용
                        cb.like(u1.get("username"), "%" + kw + "%"),    // 질문 작성자
                        cb.like(a.get("content"), "%" + kw + "%"),      // 답변 내용
                        cb.like(u2.get("username"), "%" + kw + "%"));   // 답변 작성자
            }
        };
    }

    public List<Question3> getList() {
        return this.question3Repository.findAll();
    }

    public Question3 getQuestion3(Integer id) {
        Optional<Question3> question3 = this.question3Repository.findById(id);
        if (question3.isPresent()) {
            return question3.get();
        } else {
            throw new DataNotFoundException("question not found");
        }
    }

    public Page<Question3> getList(int page, String kw) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 30, Sort.by(sorts));
        Specification<Question3> spec = search(kw);
        return this.question3Repository.findAll(spec, pageable);
    }

    public void create(String subject, String content, SiteUser user) {
        Question3 q = new Question3();
        q.setSubject(subject);
        q.setContent(content);
        q.setCreateDate(LocalDateTime.now());
        q.setAuthor(user);
        this.question3Repository.save(q);
    }

    public void modify(Question3 question3, String subject, String content) {
        question3.setSubject(subject);
        question3.setContent(content);
        question3.setModifyDate(LocalDateTime.now());
        this.question3Repository.save(question3);
    }

    public void delete(Question3 question3) {
        this.question3Repository.delete(question3);
    }

    public void vote(Question3 question3, SiteUser siteUser) {
        question3.getVoter().add(siteUser);
        this.question3Repository.save(question3);
    }
}
