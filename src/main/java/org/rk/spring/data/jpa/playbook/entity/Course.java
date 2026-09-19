package org.rk.spring.data.jpa.playbook.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Version
    private Integer version;

    @Column(name = "title")
    private String title;

    @Column(name = "credit")
    private Integer credit;

    @OneToOne(mappedBy = "course")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private CourseMaterial courseMaterial;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id", referencedColumnName = "teacherId")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Teacher teacher;
}
