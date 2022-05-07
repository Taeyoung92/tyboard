package com.web.tyboard.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Question3Repository extends JpaRepository<Question3, Integer> {
    Question3 findBySubject(String subject);
    Question3 findBySubjectAndContent(String subject, String content);
    List<Question3> findBySubjectLike(String subject);
    Page<Question3> findAll(Pageable pageable);
    Page<Question3> findAll(Specification<Question3> spec, Pageable pageable);
}
