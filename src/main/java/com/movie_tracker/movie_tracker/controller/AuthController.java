package com.movie_tracker.movie_tracker.controller;

import com.movie_tracker.movie_tracker.models.LoginUser;
import com.movie_tracker.movie_tracker.models.User;
import com.movie_tracker.movie_tracker.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.util.ClassUtils.isPresent;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@RequestBody User request){

        if(userRepository.findByEmail(request.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(request.getPassword());

        return ResponseEntity.ok("User successfully registered.");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginUser request,
                                   HttpServletRequest httpRequest){

        User user = userRepository.findByEmail(request.getEmail()).orElse(null);

        if(user == null || !user.getPassword().equals(request.getPassword())){
            return ResponseEntity.status(401).body("Invalid Credentials");
        }

        HttpSession session = httpRequest.getSession(true);

        session.setAttribute("userId", user.getId());

        return ResponseEntity.ok("Login successfully.");
    }
}
