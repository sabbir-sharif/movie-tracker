package com.movie_tracker.movie_tracker.service;

import com.movie_tracker.movie_tracker.models.User;
import com.movie_tracker.movie_tracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ResetPasswordEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendVerificationMail(String to, String token, String subject, String text) {

        String link = "http://localhost:8080/reset-password.html?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text + "\n" + link);

        mailSender.send(message);
    }
}