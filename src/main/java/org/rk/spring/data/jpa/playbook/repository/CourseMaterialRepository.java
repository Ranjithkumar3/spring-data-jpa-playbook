package org.rk.spring.data.jpa.playbook.repository;

import org.rk.spring.data.jpa.playbook.entity.CourseMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseMaterialRepository extends JpaRepository<CourseMaterial, Long> {}
