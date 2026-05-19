package com.movie_tracker.movie_tracker.controller;

import com.movie_tracker.movie_tracker.dto.SignUpRequestDto;
import com.movie_tracker.movie_tracker.models.User;
import com.movie_tracker.movie_tracker.models.VerificationToken;
import com.movie_tracker.movie_tracker.repository.UserRepository;
import com.movie_tracker.movie_tracker.repository.VerificationTokenRepository;
import com.movie_tracker.movie_tracker.service.EmailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private EmailService emailService;

    @GetMapping("/verify")
    public ResponseEntity<String> verify(@RequestParam String token) {

        VerificationToken verificationToken =
                tokenRepository.findByToken(token)
                        .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest().body("Token expired");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        tokenRepository.delete(verificationToken);

        return ResponseEntity.ok("Email verified successfully");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup_new(@Valid @RequestBody SignUpRequestDto request){

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword()); // later hash it
        user.setEnabled(false);

        userRepository.save(user);

        // Change this part into authService later. too much logic in controller
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));

        tokenRepository.save(verificationToken);

        emailService.sendVerificationMail(user.getEmail(), token);

        return ResponseEntity.ok("User registered successfully");
    }

//    @PostMapping("/signup")
//    public ResponseEntity<?> signup(@RequestBody User user) {
//
//        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
//
//        if (existingUser.isPresent()) {
//            return ResponseEntity.badRequest().body("Email already exists");
//        }
//
//        // Plain text password is used
//        userRepository.save(user);
//
//        return ResponseEntity.ok("User registered successfully");
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User requestUser,
                                   HttpServletRequest request) {

        Optional<User> userOptional =
                userRepository.findByEmail(requestUser.getEmail());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid email");
        }

        User user = userOptional.get();//get the user inside optional container

        if (!user.getPassword().equals(requestUser.getPassword())) {
            return ResponseEntity.status(401).body("Invalid password");
        }

        if (!user.isEnabled()) {
            return ResponseEntity.badRequest()
                    .body("Please verify your email first.");
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("userId", user.getId());

        return ResponseEntity.ok("Login successful");
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpSession session) {
        Integer userId = (Integer) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findById(userId).orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).build();
        }

        return ResponseEntity.ok(user); // return full user
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out");
    }
}
