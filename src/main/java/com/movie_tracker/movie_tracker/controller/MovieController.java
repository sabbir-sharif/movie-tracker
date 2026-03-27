package com.movie_tracker.movie_tracker.controller;

import com.movie_tracker.movie_tracker.models.Movie;
import com.movie_tracker.movie_tracker.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies(){
        List<Movie> movies = movieService.getAllMovies();
        return movies;
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
