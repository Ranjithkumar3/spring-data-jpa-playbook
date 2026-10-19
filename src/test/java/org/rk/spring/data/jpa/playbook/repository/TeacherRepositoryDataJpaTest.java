package org.rk.spring.data.jpa.playbook.repository;

import org.junit.jupiter.api.Test;
import org.rk.spring.data.jpa.playbook.entity.Course;
import org.rk.spring.data.jpa.playbook.entity.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class TeacherRepositoryDataJpaTest {
    @Autowired
    TeacherRepository teacherRepository;

    @Test
    void savesAndFindsTeacher() {
        List<Course> courses = new ArrayList<>();

        Course course1 = Course.builder()
                .title("PHP")
                .credit(9)
                .build();

        Course course2 = Course.builder()
                .title("Laravel")
                .credit(7)
                .build();

        Teacher teacher = Teacher.builder()
                .firstName("Jane")
                .lastName("Doe")
                .build();

        teacher.addCourse(course1);
        teacher.addCourse(course2);

        Teacher createdTeacher = teacherRepository.save(teacher);

        Teacher newTeacher = teacherRepository.findById(createdTeacher.getTeacherId()).orElseThrow();

        assertEquals(createdTeacher.getTeacherId(), newTeacher.getTeacherId());
    }
}
