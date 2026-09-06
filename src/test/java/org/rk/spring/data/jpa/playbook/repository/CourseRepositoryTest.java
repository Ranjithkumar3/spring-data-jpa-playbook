package org.rk.spring.data.jpa.playbook.repository;

import org.junit.jupiter.api.Test;
import org.rk.spring.data.jpa.playbook.entity.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseRepositoryTest {
    @Autowired
    private CourseRepository courseRepository;

    @Test
    void printAllCourses() {
        List<Course> courses = courseRepository.findAll();

        System.out.println(courses);
    }

    @Test
    void findAllPagination() {
        Pageable pageZeroWithThreeRecords = PageRequest.of(0, 3);
        Pageable pageOneWithTwoRecords = PageRequest.of(1, 2);

        List<Course> courses = courseRepository.findAll(pageOneWithTwoRecords).getContent();

        System.out.println(courses);
    }

    @Test
    void findAllPaginationAndSort() {
        Pageable sortByTitle = PageRequest.of(0, 5, Sort.by("title"));
        Pageable sortByCreditDesc = PageRequest.of(0, 5, Sort.by("credit").descending());
        Pageable sortByTitleAndCreditDesc = PageRequest.of(0, 5, Sort.by("title").and(Sort.by("credit").descending()));

        List<Course> courses = courseRepository.findAll(sortByCreditDesc).getContent();

        System.out.println("courses = " + courses);
    }

    @Test
    void findAllPageContaining() {
        Pageable records = PageRequest.of(0, 10);

        List<Course> courses = courseRepository.findByTitleContaining("D", records);

        System.out.println(courses);
    }
}