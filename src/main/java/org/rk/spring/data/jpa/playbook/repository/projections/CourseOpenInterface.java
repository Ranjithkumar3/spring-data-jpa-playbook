package org.rk.spring.data.jpa.playbook.repository.projections;

import org.springframework.beans.factory.annotation.Value;

public interface CourseOpenInterface {
    Long getCourseId();

    @Value("#{target.title + ' -> ' + (target.teacher != null ? target.teacher.firstName : 'No Teacher')}")
    String getCustomLabel();
}
