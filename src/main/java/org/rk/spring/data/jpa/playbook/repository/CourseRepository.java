package org.rk.spring.data.jpa.playbook.repository;

import org.rk.spring.data.jpa.playbook.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> { }
