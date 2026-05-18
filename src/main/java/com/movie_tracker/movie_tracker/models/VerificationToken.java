package com.movie_tracker.movie_tracker.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime expirayDate;

    public VerificationToken() {
    }

    public VerificationToken(int id, String token, User user, LocalDateTime expirayDate) {
        this.id = id;
        this.token = token;
        this.user = user;
        this.expirayDate = expirayDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpirayDate() {
        return expirayDate;
    }

    public void setExpirayDate(LocalDateTime expirayDate) {
        this.expirayDate = expirayDate;
    }
}
