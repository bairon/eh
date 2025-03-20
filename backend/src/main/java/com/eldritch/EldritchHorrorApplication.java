package com.eldritch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.eldritch.model") // Scan for JPA entities in com.eldritch.model
@EnableJpaRepositories(basePackages = "com.eldritch.da") // Scan for JPA repositories in com.eldritch.da
public class EldritchHorrorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EldritchHorrorApplication.class, args);
    }
}