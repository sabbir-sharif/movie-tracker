package com.movie_tracker.movie_tracker.controller;

import com.movie_tracker.movie_tracker.models.User;
import com.movie_tracker.movie_tracker.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;


    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody User user) {

        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User requestUser,
                                   HttpServletRequest request) {

        Optional<User> userOptional =
                userRepository.findByEmail(requestUser.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email");
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(requestUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid password");
        }


        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());

        return ResponseEntity.ok("Login successful");
    }
}