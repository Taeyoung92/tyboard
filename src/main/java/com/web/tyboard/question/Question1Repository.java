package com.web.tyboard.question;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Question1Repository extends JpaRepository<Question1, Integer> {
    Question1 findBySubject(String subject);
    Question1 findBySubjectAndContent(String subject, String content);
    List<Question1> findBySubjectLike(String subject);
    Page<Question1> findAll(Pageable pageable);
    Page<Question1> findAll(Specification<Question1> spec, Pageable pageable);
}
