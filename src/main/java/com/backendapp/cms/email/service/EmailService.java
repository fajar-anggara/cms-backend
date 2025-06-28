package com.backendapp.cms.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendHttpEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("kakikukakuko@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
            System.out.println("HTML Email berhasil dikirim ke " + to + " dengan subject: " + subject);
        } catch (MessagingException e) {
            System.err.println("Gagal mengirim HTML email ke " + to + ". Error: " + e.getMessage());

            throw new RuntimeException("Gagal mengirim HTTP email.", e);
        }
    }
}
