package org.rk.spring.data.jpa.playbook.repository;

import org.rk.spring.data.jpa.playbook.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContaining(String seq, Pageable pageRequest);
}
