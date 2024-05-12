package org.example.individualbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class IndividualBackEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(IndividualBackEndApplication.class, args);
    }
}