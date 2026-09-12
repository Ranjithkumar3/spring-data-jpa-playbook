package org.rk.spring.data.jpa.playbook.repository;

import org.rk.spring.data.jpa.playbook.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByTitleContaining(String seq, Pageable pageRequest);

    @Override
    @EntityGraph(attributePaths = {"teacher", "courseMaterial"})
    List<Course> findAll();

    /*    JOIN FETCH solution to resolve N+1 query problem

    Output with the N+1 problem:

    -- STARTING FETCH ALL ---
        Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0
        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
        Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
        Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
        Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
        --- ACCESSING RELATIONSHIPS ---
        Course: DBA taught by Shabir
        Course: SQL taught by Shabir
        Course: PHP taught by Jane
        Course: Laravel taught by Jane
        Course: Algebra taught by Srinivas
        Course: Maths taught by Srinivas
        Course: Statistics taught by Srinivas
      --- FETCH COMPLETE ---

      To write a JPQL JOIN FETCH query directly from your logs, you need to follow a 3-step mechanical process.
      You will analyze the root query, extract the child tables from the secondary queries, and translate them directly into your Java entity field paths.

      Step 1: Find your Root Entity (The FROM clause)Look at the very first query in your log:
          Hibernate: select c1_0.course_id, c1_0.credit, c1_0.teacher_id, c1_0.title from course c1_0

          What this tells you: The root table is course.
          The JPQL Translation: Find the Java @Entity class mapped to this table. That is your Course entity. We will alias it as c.

          Your JPQL blueprint starts as:
          SELECT c FROM Course c

      Step 2: Identify the Child Properties from the SQL Storm

          Look at the WHERE conditions of the extra queries that fired right after. They are targeting specific foreign keys to look up related records.

          2.1. Look at the course_material queries:
          ... from course_material cm1_0 ... where cm1_0.course_id=?

          This is looking up data from the course_material table linked to your course.
          Open your Course.java entity class.
          Look for the field mapped to this table.
          It is courseMaterial.

          2.2. Look at the teacher queries:
          ... from teacher t1_0 where t1_0.teacher_id=?

          This is looking up data from the teacher table linked to your course.
          Open your Course.java entity class.
          Look for the field mapped to this table.
          It is teacher.

      Step 3: Combine them into JPQL using LEFT JOIN FETCH

      Now, append these fields to your root blueprint using LEFT JOIN FETCH.

      Senior Tip:
          Always use LEFT JOIN FETCH instead of an inner JOIN FETCH.
          An inner join will completely hide any Course that doesn't have a teacher or doesn't have a course material assigned yet.
          A LEFT JOIN ensures all courses come back, regardless of whether their relationships are empty.
          Stitch them together by referencing your root alias c followed by the exact Java field names:

      SELECT c FROM Course c LEFT JOIN FETCH c.teacher LEFT JOIN FETCH c.courseMaterial
    */
    @Query("SELECT c FROM Course c LEFT JOIN FETCH c.teacher LEFT JOIN FETCH c.courseMaterial")
    List<Course> findAllCoursesUsingJoinFetch();
}
