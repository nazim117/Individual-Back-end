package org.example.individualbackend.business.notifications_service.implementation;

import lombok.AllArgsConstructor;
import org.example.individualbackend.business.notifications_service.interfaces.NotificationsUseCase;
import org.springframework.mail.MailSendException;
import org.springframework.stereotype.Service;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
@AllArgsConstructor
public class NotificationsUseCaseImpl implements NotificationsUseCase {
    private final JavaMailSender mailSender;

    @Override
    public void sendEmail(String toEmail, String subject, String body){
        if(toEmail.isEmpty()){
            throw new IllegalArgumentException("Email cannot be empty");
        }
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("nazimcompany31@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);

            mailSender.send(message);
        }catch (Exception e){
            throw new MailSendException("Error sending email: " + e.getMessage());
        }
    }
}
