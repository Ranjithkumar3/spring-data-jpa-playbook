package org.rk.spring.data.jpa.playbook.repository;

import org.junit.jupiter.api.Test;
import org.rk.spring.data.jpa.playbook.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;

    @Test
    void printAllCourses() {
        List<Course> courses = courseRepository.findAll();

        System.out.println(courses);
    }
}