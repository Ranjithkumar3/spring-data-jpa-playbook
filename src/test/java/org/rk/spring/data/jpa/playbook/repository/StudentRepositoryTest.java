package org.rk.spring.data.jpa.playbook.repository;

import org.junit.jupiter.api.Test;
import org.rk.spring.data.jpa.playbook.entity.Course;
import org.rk.spring.data.jpa.playbook.entity.Guardian;
import org.rk.spring.data.jpa.playbook.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

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

    @Test
    public void testJPAAuditing() throws InterruptedException {
        Guardian guardian = Guardian.builder()
                .name("Durga")
                .mobile("5550100002")
                .email("jd@jpa.com")
                .build();

        Course course = courseRepository.findById(1L)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        Student student = Student.builder()
                .firstName("Vkrm")
                .lastName("Aditya")
                .emailId("va@jpa.com")
                .guardian(guardian)
                .build();

        student.addCourse(course);

        System.out.println("=== EXECUTING INSERT ===");
        Student savedStudent = studentRepository.saveAndFlush(student);

        LocalDateTime initialCreateTime = savedStudent.getCreatedAt();
        LocalDateTime initialModifyTime = savedStudent.getLastModifiedAt();

        assertThat(initialCreateTime).isNotNull();
        assertThat(initialModifyTime).isNotNull();
        assertThat(initialCreateTime).isEqualTo(initialModifyTime);
        System.out.println("Entity created at: " + initialCreateTime);

        System.out.println("Sleeping for 2 seconds to create a temporal delta...");
        Thread.sleep(2000);

        savedStudent.setLastName("Smith"); // Change a basic field
        System.out.println("=== EXECUTING UPDATE ===");
        Student updatedStudent = studentRepository.saveAndFlush(savedStudent);

        assertThat(updatedStudent.getCreatedAt()).isEqualTo(initialCreateTime);

        assertThat(updatedStudent.getLastModifiedAt()).isAfter(initialModifyTime);

        System.out.println("Verification Complete:");
        System.out.println("Immutable Created At: " + updatedStudent.getCreatedAt());
        System.out.println("Advanced Modified At: " + updatedStudent.getLastModifiedAt());

        /*Hibernate: select c1_0.course_id,cm1_0.course_material_id,cm1_0.course_id,cm1_0.url,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title from course c1_0 left join course_material cm1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where c1_0.course_id=?
        === EXECUTING INSERT ===
        Hibernate: select nextval('student_sequence')
        Hibernate: insert into jpa_student (created_at,email_address,first_name,guardian_email,guardian_mobile,guardian_name,last_modified_at,last_name,student_id) values (?,?,?,?,?,?,?,?,?)
        Hibernate: insert into student_course_map (student_id,course_id) values (?,?)
        Entity created at: 2026-09-19T15:03:56.980469
        Sleeping for 2 seconds to create a temporal delta...
        === EXECUTING UPDATE ===
        Hibernate: select s1_0.student_id,s1_0.created_at,s1_0.email_address,s1_0.first_name,s1_0.guardian_email,s1_0.guardian_mobile,s1_0.guardian_name,s1_0.last_modified_at,s1_0.last_name from jpa_student s1_0 where s1_0.student_id=?
        Hibernate: select c1_0.student_id,c1_1.course_id,cm1_0.course_material_id,cm1_0.course_id,cm1_0.url,c1_1.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_1.title from student_course_map c1_0 join course c1_1 on c1_1.course_id=c1_0.course_id left join course_material cm1_0 on c1_1.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_1.teacher_id where c1_0.student_id=?
        Hibernate: update jpa_student set email_address=?,first_name=?,guardian_email=?,guardian_mobile=?,guardian_name=?,last_modified_at=?,last_name=? where student_id=?
        Verification Complete:
        Immutable Created At: 2026-09-19T15:03:56.980469
        Advanced Modified At: 2026-09-19T15:03:59.026822
         */
    }


}