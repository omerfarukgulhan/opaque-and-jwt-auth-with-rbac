package com.ofg.auth.service.abstracts;

public interface EmailService {
    void sendActivationEmail(String email, String activationToken);

    void sendPasswordResetEmail(String email, String passwordResetToken);

    void sendEmail(String to, String body, String subject);
}
