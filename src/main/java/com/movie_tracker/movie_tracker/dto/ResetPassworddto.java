package com.movie_tracker.movie_tracker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class ResetPassworddto {
    @NotBlank
    @Email
    private String email;

    public ResetPassworddto() {
    }

    public ResetPassworddto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
