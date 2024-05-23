package org.example.individualbackend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestMailSenderConfiguration {
    @Bean
    public JavaMailSender javaMailSender() {
        return mock(JavaMailSender.class);
    }

}
