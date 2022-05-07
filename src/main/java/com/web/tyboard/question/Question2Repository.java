package com.web.tyboard.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Question2Repository extends JpaRepository<Question2, Integer> {
    Question2 findBySubject(String subject);
    Question2 findBySubjectAndContent(String subject, String content);
    List<Question2> findBySubjectLike(String subject);
    Page<Question2> findAll(Pageable pageable);
    Page<Question2> findAll(Specification<Question2> spec, Pageable pageable);
}
