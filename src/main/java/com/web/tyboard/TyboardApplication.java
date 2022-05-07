package com.web.tyboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TyboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(TyboardApplication.class, args);
    }

}
