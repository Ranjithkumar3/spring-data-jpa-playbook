package org.rk.spring.data.jpa.playbook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "teacher")
    private List<Course> courses;

    // Inside your Teacher class
    public void addCourse(Course course) {
        if (this.courses == null) {
            this.courses = new java.util.ArrayList<>();
        }
        this.courses.add(course);
        course.setTeacher(this); // Crucial: Keeps the owning side in sync!
    }
}
