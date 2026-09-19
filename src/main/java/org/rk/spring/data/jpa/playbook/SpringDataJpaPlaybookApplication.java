package org.rk.spring.data.jpa.playbook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SpringDataJpaPlaybookApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringDataJpaPlaybookApplication.class, args);
    }

}
