package org.rk.spring.data.jpa.playbook.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Optional;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="jpa_student")
public class Student {
    @Id()
    @Column(name = "student_id")
    @SequenceGenerator(name = "student_sequence", sequenceName = "student_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "student_sequence")
    private Long studentId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailId;

    @Embedded
    private Guardian guardian;

    @ManyToMany
    @JoinTable(
            name = "student_course_map",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @ToString.Exclude
    private List<Course> courses;

    public void addCourse(Course course) {
        if (this.courses == null) {
            this.courses = new java.util.ArrayList<>();
        }
        this.courses.add(course);
    }

}
