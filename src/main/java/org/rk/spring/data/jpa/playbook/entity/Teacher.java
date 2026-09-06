package org.rk.spring.data.jpa.playbook.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    @ToString.Exclude
    // JPA TRAP: 'Course' owns the relationship (holds the @JoinColumn).
    // If you only add courses to the Teacher's list, Hibernate will insert 'teacher_id' as NULL.
    // THE FIX: You must explicitly set the teacher on each course (e.g., course.setTeacher(teacher))
    // to ensure the foreign key is correctly persisted in the database.
    private List<Course> courses;

    // Fix for the JPA trap
    public void addCourse(Course course) {
        if (this.courses == null) {
            this.courses = new java.util.ArrayList<>();
        }
        this.courses.add(course);
        course.setTeacher(this); // Crucial: Keeps the owning side in sync!
    }
}
