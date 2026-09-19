package org.rk.spring.data.jpa.playbook.repository;

import org.junit.jupiter.api.Test;
import org.rk.spring.data.jpa.playbook.entity.Course;
import org.rk.spring.data.jpa.playbook.entity.CourseMaterial;
import org.rk.spring.data.jpa.playbook.entity.Teacher;
import org.rk.spring.data.jpa.playbook.repository.projections.CourseOpenInterface;
import org.rk.spring.data.jpa.playbook.repository.projections.CourseRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.List;

@SpringBootTest
public class CoursePerformanceTest {
    @Autowired TeacherRepository teacherRepository;
    @Autowired CourseMaterialRepository courseMaterialRepository;
    @Autowired CourseRepository courseRepository;

    @Test
    void createTeacher() {
        CourseMaterial courseMaterial = courseMaterialRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("Course material not found"));

        Course course1 = Course.builder()
                .title("Algebra")
                .credit(3)
                .courseMaterial(courseMaterial)
                .build();

        Course course2 = Course.builder()
                .title("Maths")
                .credit(4)
                .courseMaterial(courseMaterial)
                .build();

        Course course3 = Course.builder()
                .title("Statistics")
                .credit(3)
                .courseMaterial(courseMaterial)
                .build();

        Teacher teacher = Teacher.builder()
                .firstName("Srinivas")
                .lastName("Rao")
                .build();

        teacher.addCourse(course1);
        teacher.addCourse(course2);
        teacher.addCourse(course3);

        teacherRepository.save(teacher);
    }

    @Test
    void triggerNPlus1Problem() {
        System.out.println("--- STARTING FETCH ALL ---");
        List<Course> courses = courseRepository.findAll();

        System.out.println("--- ACCESSING RELATIONSHIPS ---");
        for (Course course : courses) {
            // Access the teacher to force initialization if it's lazy
            if (course.getTeacher() != null) {
                System.out.println("Course: " + course.getTitle() + " taught by " + course.getTeacher().getFirstName());
            }
        }
        System.out.println("--- FETCH COMPLETE ---");

        //Output before the EntityGraph fix (clearly showing the N+1 problem.):

//        --- STARTING FETCH ALL ---
//                Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0
//        Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
//                Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
//                Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
//                Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
//                Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
//                Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
//                Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
//                Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
//                Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
//                Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
//                Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
//                Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
//                Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
//                --- ACCESSING RELATIONSHIPS ---
//                Course: DBA taught by Shabir
//        Course: SQL taught by Shabir
//        Course: PHP taught by Jane
//        Course: Laravel taught by Jane
//        Course: Algebra taught by Srinivas
//        Course: Maths taught by Srinivas
//        Course: Statistics taught by Srinivas
//        --- FETCH COMPLETE ---

//        Justification and Debugging:
//
//
//        Let's dissect exactly what your 13 queries represent:

//          Step 1: Breakdown of Log
//
    //            1. The initial query (1):
    //            select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0;
    //
    //            What it means: This is your courseRepository.findAll(). It fetches all 7 rows from the course table
    //
    //            2. The CourseMaterial Storm (9 queries):
    //            You have multiple queries looking exactly like this:
    //            select cm1_0.course_material_id... from course_material cm1_0... where cm1_0.course_id=?;
    //
    //            What it means: Because your @OneToOne relationship on courseMaterial is bidirectional (mappedBy = "course") and defaults to EAGER, Hibernate is forced to query the course_material table for every single course it just loaded to see if an associated record exists.
    //            Note:
    //            You have 7 courses in your database, but your log shows 9 material queries. Why?It happens because your Course and CourseMaterial entities have a two-way relationship (bidirectional mapping), and both sides are set to EAGER.
    //
    //            Queries 9 and 10 (The extra 2 queries): Because both sides of the relationship are fighting to be loaded immediately (EAGER), Hibernate gets confused. It wonders: "Wait, is the Course data I loaded in Step 1 exactly identical to the Course data I just pulled inside this Material join?" To be absolutely safe and prevent data bugs, it fires 2 extra double-check queries to re-verify the records in the database.
    //            When you mix Bidirectional Mappings with EAGER fetching, Hibernate loses its optimization efficiency. It starts firing extra "double-check" queries, resulting in more queries than you actually have rows.
    //
    //
    //            3. The Teacher Lookups (3 queries):
    //            select t1_0.teacher_id... from teacher t1_0 where t1_0.teacher_id=?;
    //
    //            What it means: Your @ManyToOne relationship to Teacher is also EAGER. Hibernate sees that the 7 courses belong to 3 distinct teacher IDs (Shabir, Jane, Srinivas). It fires 3 separate queries to fetch those 3 unique teachers.

//            Step 2: Debug
//
//                2.1. Identify the "Target Repository Method"
//                    Look at your code to see which repository method call is printing the SQL storm.
//                    In your current case, the logs started spitting out queries right after --- STARTING FETCH ALL ---.
//                    This means the target method is courseRepository.findAll(). That is the specific method you need to override or customize in your repository interface.
//
//                2.2. Identify the "Attribute Paths" to include
//                    Look closely at the WHERE clauses of the secondary queries in your log to see which tables are being targeted.
//                    From your log:You saw queries targeting where cm1_0.course_id=? -> This is looking up the courseMaterial relation.
//                    You saw queries targeting where t1_0.teacher_id=? -> This is looking up the teacher relation.
//                    Now, look at your Course entity class and match those table lookups to your Java field names:
//                        course_material maps to your Java field private CourseMaterial courseMaterial;
//                        teacher maps to your Java field private Teacher teacher;These field names are your attributePaths.
//
//                    Therefore, your annotation must look exactly like this:
//                    @EntityGraph(attributePaths = {"teacher", "courseMaterial"})

//                Step 3: Fix - Map the Entity Graph to the Repository
//
//                    Go to the repository interface that manages the root entity (in this case, CourseRepository).
//                    Define or override the target method from Step 1, and attach the @EntityGraph configuration you built in Step 2:
//
    //                public interface CourseRepository extends JpaRepository<Course, Long> {
    //                    // You match the method from Step 1, and apply the paths from Step 2
    //                    @Override
    //                    @EntityGraph(attributePaths = {"teacher", "courseMaterial"})
    //                    List<Course> findAll();
    //                }

//                    Summary Checklist for Future Debugging
//                    Whenever you see a performance drop or an N+1 problem in any project:
//                        Locate the line of code executing the repository query.
//                        Read the SQL logs to see which child tables are being selected in separate queries.
//                        Find the matching field names in your parent Java Entity class.
//                        Put those field names into an @EntityGraph directly above that repository method.

//        Output after the fix:
//
//        --- STARTING FETCH ALL ---
//        Hibernate: select c1_0.course_id,cm1_0.course_material_id,cm1_0.course_id,cm1_0.url,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title from course c1_0 left join course_material cm1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id
//        --- ACCESSING RELATIONSHIPS ---
//        Course: DBA taught by Shabir
//        Course: SQL taught by Shabir
//        Course: PHP taught by Jane
//        Course: Laravel taught by Jane
//        Course: Algebra taught by Srinivas
//        Course: Maths taught by Srinivas
//        Course: Statistics taught by Srinivas
//        --- FETCH COMPLETE ---
    }

    @Test
    void verifyJoinFetchPerformance() {
        System.out.println("--- STARTING JPQL JOIN FETCH ---");
        List<Course> courses = courseRepository.findAllCoursesUsingJoinFetch();

        System.out.println("--- ACCESSING RELATIONSHIPS ---");
        for (Course course : courses) {
            if (course.getTeacher() != null) {
                System.out.println("Course: " + course.getTitle() + " taught by " + course.getTeacher().getFirstName());
            }
        }
        System.out.println("--- FETCH COMPLETE ---");
    }

    @Test
    void auditHeavyLoadingEntityProblem() {
        // --- TEST 0: The Unoptimized Entity Load ---
        System.out.println("=== EXECUTING COMPLETE ENTITY LOAD ===");
        // Pulls every column, manages it in heap memory, and triggers relationship joins/queries
        List<Course> completeEntities = courseRepository.findByCredit(3, Course.class);
        for (Course course : completeEntities) {
            // Relying on default entity fields or triggering LAZY proxies
            String teacherName = (course.getTeacher() != null) ? course.getTeacher().getFirstName() : "No Teacher";
            System.out.println("Entity: " + course.getTitle() + " by " + teacherName);
        }

        /* Output:
            === EXECUTING COMPLETE ENTITY LOAD ===
            Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0 where c1_0.credit=?
            Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
            Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
            Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?

            Entity: Algebra by Srinivas
            Entity: Statistics by Srinivas
        */
    }

    @Test
    void auditProjectionSql() {
        // --- TEST 1: The Optimized Record ---
        System.out.println("=== EXECUTING RECORD PROJECTION ===");
        List<CourseRecordDTO> records = courseRepository.findByCredit(3, CourseRecordDTO.class);
        for (CourseRecordDTO dto : records) {
            System.out.println("Record: " + dto.title() + " by " + dto.teacherFirstName());
        }

        // --- TEST 2: The Open Interface (SpEL) ---
        System.out.println("=== EXECUTING OPEN INTERFACE PROJECTION ===");
        List<CourseOpenInterface> interfaces = courseRepository.findByCredit(3, CourseOpenInterface.class);
        for (CourseOpenInterface minterface : interfaces) {
            System.out.println("Interface Label: " + minterface.getCustomLabel());
        }

        /* Output
            === EXECUTING RECORD PROJECTION ===
            Hibernate: select c1_0.course_id,c1_0.title,t1_0.first_name from course c1_0 left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where c1_0.credit=?

            Record: Algebra by Srinivas
            Record: Statistics by Srinivas

            === EXECUTING OPEN INTERFACE PROJECTION ===
            Hibernate: select c1_0.course_id,c1_0.credit,c1_0.teacher_id,c1_0.title from course c1_0 where c1_0.credit=?
            Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?
            Hibernate: select t1_0.teacher_id,t1_0.first_name,t1_0.last_name from teacher t1_0 where t1_0.teacher_id=?
            Hibernate: select cm1_0.course_material_id,cm1_0.course_id,c1_0.course_id,c1_0.credit,t1_0.teacher_id,t1_0.first_name,t1_0.last_name,c1_0.title,cm1_0.url from course_material cm1_0 join course c1_0 on c1_0.course_id=cm1_0.course_id left join teacher t1_0 on t1_0.teacher_id=c1_0.teacher_id where cm1_0.course_id=?

            Interface Label: Algebra -> Srinivas
            Interface Label: Statistics -> Srinivas
         */
    }

    @Test
    public void testOptimisticLockingConcurrency() {
        // 1. Arrange: Ensure a course with ID 1 exists in your database
        // (If your DB resets, insert one first and get its ID)
        Long targetCourseId = 1L;

        // 2. Act: Simulate User 1 loading the course onto their screen
        Course courseUser1 = courseRepository.findById(targetCourseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // 3. Act: Simulate User 2 loading the EXACT SAME course onto their screen a second later
        Course courseUser2 = courseRepository.findById(targetCourseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        // Verify both users currently hold the exact same version number (e.g., version = 0)
        System.out.println("User 1 baseline version: " + courseUser1.getVersion());
        System.out.println("User 2 baseline version: " + courseUser2.getVersion());

        // 4. Act: User 1 edits the title and clicks "Save" first
        courseUser1.setTitle("Title Updated By User 1");
        System.out.println("=== USER 1 SAVING ===");
        courseRepository.saveAndFlush(courseUser1);
        // This succeeds and bumps the database version to 1!

        // 5. Act: User 2 tries to edit the title and clicks "Save" second
        // Their local object still thinks the version is 0!
        courseUser2.setTitle("Title Updated By User 2");
        System.out.println("=== USER 2 SAVING ===");

        // 6. Assert: Wrap User 2's save in an assertion to prove it crashes the transaction
        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.orm.ObjectOptimisticLockingFailureException.class,
                () -> {
                    courseRepository.saveAndFlush(courseUser2);
                },
                "Expected ObjectOptimisticLockingFailureException but it did not throw!"
        );

        System.out.println("Concurrency Protection Verified! Data overwrite blocked successfully.");
    }


}
