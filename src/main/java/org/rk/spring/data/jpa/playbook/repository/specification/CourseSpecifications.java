package org.rk.spring.data.jpa.playbook.repository.specification;

import jakarta.persistence.criteria.Join;
import org.rk.spring.data.jpa.playbook.entity.Course;
import org.rk.spring.data.jpa.playbook.entity.Teacher;
import org.springframework.data.jpa.domain.Specification;

public class CourseSpecifications {
    public static Specification<Course> containsTitle(String title) {
        return (root, query, cb) ->
                (title == null || title.isBlank()) ? null : cb.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<Course> hasCredit(Integer credit) {
        return (root, query, cb) -> (credit == null) ? null : cb.equal(root.get("credit"), credit);
    }

    /* The Cross-Table Relation Filter (Filtering by Child Fields)
        Generated SQL query:

        SELECT c1_0.*
        FROM course c1_0
        INNER JOIN teacher t1_0 ON c1_0.teacher_id = t1_0.teacher_id
        WHERE lower(t1_0.last_name) LIKE 's%';
     */
    public static Specification<Course> hasTeacherLastNameStartingWith(String prefix) {
        return (root, query, cb) -> {
            if(prefix == null || prefix.isBlank()) {
                return null;
            }

            Join<Course, Teacher> teacherJoin = root.join("teacher");

            return cb.like(cb.lower(teacherJoin.get("lastName")), prefix.toLowerCase() + "%");
        };
    }

    /* Excercise 2B: The Collection Sub-Query Filter (Multi-Select Grouping)Imagine the frontend UI passes a list of multiple specific credit numbers via check-boxes (e.g., the user checks fields for courses that are exactly 3 OR 5 credits).
    Passing list constraints requires moving past basic comparison operators.
    Your Task:Add a specification method called hasCreditsIn(List<Integer> allowedCredits).
    Inside the lambda, handle safety checks: if the list is null or completely empty, return null so the filter is ignored.
    If the list contains values, use the root.get("credit").in(allowedCredits) or cb.in() syntax to construct a dynamic set constraint.
    Write a test passing List.of(3, 5) and look for the native SQL IN (?, ?) block expression generated in your terminal output.
     */

    /* Exercise 2C: Advanced Combo Chaining (Merging Optional Groups)In a real enterprise search dashboard, filters often combine using complex nested boolean rules instead of flat matching strings.
    Let's build a rule: "Find all courses that match either (Title contains 'Math' AND Credit is at least 3) OR (the course title contains 'Data').
    "Your Task:Write a test method that instantiates three separate specifications using your existing basic selectors: specTitleMath, specCreditThree, and specTitleData.
    Use a combination of .and() and .or() syntax groupings to chain these together cleanly in a single execution line.
    The Trap to watch out for: Pay close attention to standard operator precedence rules in Java.
    You may need to group specific criteria blocks using Specification.where(...) sub-wrappers to ensure the generated SQL injects structural parentheses ( ... AND ... ) OR ... correctly around the conditional blocks.
    Run the query and verify the database log matches your exact logical boundaries.
     */

}
