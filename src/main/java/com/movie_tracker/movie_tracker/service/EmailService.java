package com.movie_tracker.movie_tracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationMail(String to, String token){

        String link = "http://localhost:8080/auth/verify?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Verification your CineLog account.");
        message.setText("Click the link to verify your account:\n" + link);

        mailSender.send(message);
    }
}
