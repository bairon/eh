package com.eldritch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.eldritch") // Scan for JPA entities in com.eldritch.model
@EnableJpaRepositories(basePackages = "com.eldritch") // Scan for JPA repositories in com.eldritch.da
@EnableCaching
public class EldritchHorrorApplication {
    public static void main(String[] args) {
        SpringApplication.run(EldritchHorrorApplication.class, args);
    }
}