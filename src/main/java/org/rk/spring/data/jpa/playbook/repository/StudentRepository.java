package org.rk.spring.data.jpa.playbook.repository;

import jakarta.transaction.Transactional;
import org.rk.spring.data.jpa.playbook.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByFirstName(String firstName);
    List<Student> findByFirstNameContaining(String firstName);

    //JPQL
    @Query("select s from Student s where s.emailId = ?1")
    Student getStudentByEmailAddress(String emailId);

    @Query("select s.firstName from Student s where s.emailId = ?1")
    String getStudentFirstNameByEmailAddress(String emailId);

    //Native query
    @Query(value = "SELECT * FROM jpa_student s WHERE s.email_address = ?1", nativeQuery = true)
    Student getStudentByEmailAddressNative(String emailId);

    @Query(value = "SELECT * FROM jpa_student s WHERE s.email_address = :emailId", nativeQuery = true)
    Student getStudentByEmailAddressNativeNamedParam(@Param("emailId") String emailId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE jpa_student SET first_name = :firstName WHERE email_address = :emailId", nativeQuery = true)
    int updateStudentNameByEmaiId(@Param("firstName") String firstName, @Param("emailId") String emailId);
}
