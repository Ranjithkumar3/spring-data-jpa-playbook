package org.rk.spring.data.jpa.playbook.repository;

import org.junit.jupiter.api.Test;
import org.rk.spring.data.jpa.playbook.entity.Course;
import org.rk.spring.data.jpa.playbook.entity.Guardian;
import org.rk.spring.data.jpa.playbook.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Test
    public void saveStudent() {
        Guardian guardian = Guardian.builder()
                .email("srijd@jpa.com")
                .name("srijd")
                .mobile("5550100001")
                .build();

        Student student = Student.builder()
                .emailId("rkjd@jpa.com")
                .firstName("Rku")
                .lastName("Jdy")
                .guardian(guardian)
                .build();

        studentRepository.save(student);
    }

    @Test
    public void printAllStudents() {
        List<Student> studentList = studentRepository.findAll();

        System.out.println("Student List: " + studentList);
    }

    @Test
    public void printStudentByFirstName() {
        Student student = studentRepository.findByFirstName("Rk");

        System.out.println(student);
    }

    @Test
    public void printStudentByFirstNameContaining() {
        List<Student> student = studentRepository.findByFirstNameContaining("Rk");

        System.out.println(student);
    }

    @Test
    public void getStudentByEmailAddress() {
        Student student = studentRepository.getStudentByEmailAddress("rku@jpa.com");

        System.out.println(student);
    }

    @Test
    public void getStudentFirstNameByEmailAddress() {
        String studentName = studentRepository.getStudentFirstNameByEmailAddress("rku@jpa.com");

        System.out.println(studentName);
    }

    @Test
    public void getStudentByEmailAddressNative() {
        Student student = studentRepository.getStudentByEmailAddressNative("rku@jpa.com");

        System.out.println(student);
    }

    @Test
    public void getStudentByEmailAddressNativeNamedParam() {
        Student student = studentRepository.getStudentByEmailAddressNative("rku@jpa.com");

        System.out.println(student);
    }

    @Test
    public void updateStudentNameByEmaiId() {
       studentRepository.updateStudentNameByEmaiId("Ranjith", "rku@jpa.com");
    }

    @Test
    public void createStudentWithCourses() {
        Guardian guardian = Guardian.builder()
                .name("Uday")
                .mobile("5550100001")
                .email("uday@jpa.com")
                .build();

        Course course = courseRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Student student = Student.builder()
                .firstName("Sanju")
                .lastName("Uday")
                .emailId("su@jpa.com")
                .guardian(guardian)
                .build();

        student.addCourse(course);

        studentRepository.save(student);
    }
}