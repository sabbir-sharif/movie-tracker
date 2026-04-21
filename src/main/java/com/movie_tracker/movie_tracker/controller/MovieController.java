package com.movie_tracker.movie_tracker.controller;

import com.movie_tracker.movie_tracker.models.Movie;
import com.movie_tracker.movie_tracker.models.User;
import com.movie_tracker.movie_tracker.repository.UserRepository;
import com.movie_tracker.movie_tracker.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@CrossOrigin(origins = "http://127.0.0.1:5500", allowCredentials = "true")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    private UserRepository userRepository;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    private User getLoggedInUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("userId") == null) {
            throw new RuntimeException("Unauthorized");
        }

        int userId = (int) session.getAttribute("userId");

        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }


    @GetMapping
    public ResponseEntity<?> getAllMovie(HttpServletRequest request,
                                         Pageable pageable) {
        try {
            User user = getLoggedInUser(request);
            Page<Movie> movies = movieService.getAllMovies(user, pageable);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    @PostMapping
    public ResponseEntity<?> addMovie(@RequestBody Movie movie,
                                      HttpServletRequest request) {
        try {
            User user = getLoggedInUser(request);

            Movie newMovie = new Movie();
            newMovie.setGenre(movie.getGenre());
            newMovie.setTitle(movie.getTitle());
            newMovie.setStatus(movie.getStatus());
            newMovie.setUser(user);

            Movie savedMovie = movieService.addMovie(newMovie);

            return ResponseEntity.ok(savedMovie);

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable int id,
                                         @RequestBody Movie movie,
                                         HttpServletRequest request) {
        try {
            User user = getLoggedInUser(request);

            Movie existingMovie = movieService.getById(id);

            //check owner
            if (existingMovie.getUser().getId() != user.getId()) {
                return ResponseEntity.status(403).body("Forbidden");
            }

            existingMovie.setGenre(movie.getGenre());
            existingMovie.setTitle(movie.getTitle());
            existingMovie.setStatus(movie.getStatus());

            movieService.addMovie(existingMovie);

            return ResponseEntity.ok("Updated");

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable int id,
                                         HttpServletRequest request) {
        try {
            User user = getLoggedInUser(request);

            Movie movie = movieService.getById(id);

            if (movie.getUser().getId() != user.getId()) {
                return ResponseEntity.status(403).body("Forbidden");
            }

            movieService.delete(id);

            return ResponseEntity.ok("Deleted " + movie.getTitle());

        } catch (Exception e) {
            return ResponseEntity.status(401).body("Unauthorized");
        }
    }
}
