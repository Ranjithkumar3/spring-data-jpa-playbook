package org.rk.spring.data.jpa.playbook.repository;

import org.junit.jupiter.api.Test;
import org.rk.spring.data.jpa.playbook.entity.Course;
import org.rk.spring.data.jpa.playbook.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeacherRepositoryTest {
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void saveTeacher() {
        List<Course> courses = new ArrayList<>();

        Course course1 = Course.builder()
                .title("DBA")
                .credit(8)
                .build();

        Course course2 = Course.builder()
                .title("SQL")
                .credit(7)
                .build();

        courses.add(course1);
        courses.add(course2);

        Teacher teacher = Teacher.builder()
                .firstName("Shabir")
                .lastName("Dawoodi")
                .courses(courses)
                .build();

        teacherRepository.save(teacher);
    }
}