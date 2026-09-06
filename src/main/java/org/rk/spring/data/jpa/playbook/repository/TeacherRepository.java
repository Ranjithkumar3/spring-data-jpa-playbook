package org.rk.spring.data.jpa.playbook.repository;

import org.rk.spring.data.jpa.playbook.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
}
