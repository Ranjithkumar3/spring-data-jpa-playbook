package org.rk.spring.data.jpa.playbook.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.rk.spring.data.jpa.playbook.entity.Course;
import org.rk.spring.data.jpa.playbook.entity.CourseMaterial;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseMaterialRepositoryTest {
    @Autowired
    private CourseMaterialRepository courseMaterialRepository;

    @Test
    void saveCourseMaterial() {
        Course course = Course.builder()
                .title("Java")
                .credit(10)
                .build();

        CourseMaterial courseMaterial = CourseMaterial.builder()
                .url("www.jpa.com/java")
                .course(course)
                .build();

        courseMaterialRepository.save(courseMaterial);
    }

    @Test
    void printAllCourseMaterials() {
        List<CourseMaterial> courseMaterials = courseMaterialRepository.findAll();

        System.out.println(courseMaterials);
    }
}