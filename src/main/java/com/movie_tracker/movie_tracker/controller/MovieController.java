package com.movie_tracker.movie_tracker.controller;

import com.movie_tracker.movie_tracker.models.Movie;
import com.movie_tracker.movie_tracker.models.User;
import com.movie_tracker.movie_tracker.repository.UserRepository;
import com.movie_tracker.movie_tracker.service.MovieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/movies")
public class MovieController {

    MovieService movieService;
    @Autowired
    UserRepository userRepository;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public Page<Movie> getAllMovie(HttpServletRequest request,
                                   Pageable pageable){

        int userId = (int) request.getSession(false).getAttribute("userId");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return movieService.getAllMovies(user , pageable);
    }


    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie){
        Movie newMovie = movieService.addMovie(movie);
        return ResponseEntity.ok(newMovie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateMovie(@PathVariable int id,
                                              @RequestBody Movie movie){
        Movie newMovie = movieService.getById(id);
        newMovie.setGenre(movie.getGenre());
        newMovie.setTitle(movie.getTitle());
        newMovie.setStatus(movie.getStatus());

        movieService.addMovie(newMovie);
        return ResponseEntity.ok("Updated");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable int id){
        Movie movie = movieService.getById(id);
        movieService.delete(id);
        return ResponseEntity.ok("Deleted " + movie.getTitle());
    }
}
