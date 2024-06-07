package org.example.individualbackend.business.notifications_service.implementation;

import org.example.individualbackend.config.TestMailSenderConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = TestMailSenderConfiguration.class)
class NotificationsUseCaseImplTest {
    @Mock
    private JavaMailSender mailSender;
    @InjectMocks
    private NotificationsUseCaseImpl notificationsUseCase;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void sendEmailNotification_Successful() {
        String email = "test@example.com";
        String subject = "Notification Test";
        String body = "Notification Body";

        notificationsUseCase.sendEmail(email, subject, body);

        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom("nazimcompany31@gmail.com");
        expectedMessage.setTo(email);
        expectedMessage.setSubject(subject);
        expectedMessage.setText(body);

        verify(mailSender).send(expectedMessage);
    }

    @Test
    void sendEmailNotification_Failure() {
        String email = "test@example.com";
        String subject = "Notification Test";
        String body = "Notification Body";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("nazimcompany31@gmail.com");
        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);

        doThrow(new RuntimeException("Failed to send email")).when(mailSender).send(message);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationsUseCase.sendEmail(email, subject, body);
        });

        assert exception.getMessage().contains("Failed to send email");
        verify(mailSender).send(message);
    }

    @Test
    void sendEmail_EmptyEmail_ThrowsIllegalArgumentException() {
        String email = "";
        String subject = "Notification Subject";
        String body = "Notification Body";

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            notificationsUseCase.sendEmail(email, subject, body);
        });

        assertEquals("Email cannot be empty", exception.getMessage());
        verifyNoInteractions(mailSender);
    }

}
