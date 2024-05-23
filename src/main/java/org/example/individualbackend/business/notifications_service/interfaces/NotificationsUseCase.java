package org.example.individualbackend.business.notifications_service.interfaces;

public interface NotificationsUseCase {
    void sendEmail(String toEmail, String subject, String body);
}
