package org.rk.spring.data.jpa.playbook.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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

    @PersistenceContext
    EntityManager entityManager;

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
        entityManager.flush();

        Teacher newTeacher = teacherRepository.findById(createdTeacher.getTeacherId()).orElseThrow();
        entityManager.clear();

        assertEquals(createdTeacher.getTeacherId(), newTeacher.getTeacherId());
    }

    @Test
    void savesAndFindsTeachersByLastName() {
        List<Course> courses = new ArrayList<>();

        Course course1 = Course.builder()
                .title("PHP")
                .credit(9)
                .build();

        Course course2 = Course.builder()
                .title("Laravel")
                .credit(7)
                .build();

        Teacher teacher1 = Teacher.builder()
                .firstName("Jamie")
                .lastName("Overton")
                .build();

        teacher1.addCourse(course1);
        teacher1.addCourse(course2);

        teacherRepository.save(teacher1);
        entityManager.flush();

        Teacher teacher2 = Teacher.builder()
                .firstName("Jamie")
                .lastName("Craig")
                .build();

        teacher2.addCourse(course1);
        teacher2.addCourse(course2);

        teacherRepository.save(teacher2);
        entityManager.flush();

        entityManager.clear();

        List<Teacher> overtonList = teacherRepository.findByLastName("Overton");
        assertEquals("Overton", overtonList.get(0).getLastName());
    }
}
