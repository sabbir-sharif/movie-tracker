package com.movie_tracker.movie_tracker.service;

import com.movie_tracker.movie_tracker.models.Movie;
import com.movie_tracker.movie_tracker.repository.MovieRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public Movie addMovie(Movie movie){
        return movieRepository.save(movie);
    }
    public Page<Movie> getAllMovies(Pageable pageable){
        return movieRepository.findAll(pageable);
    }
    public Movie getById(int id){
        return movieRepository.getById(id);
    }
    public void delete(int id){
        movieRepository.deleteById(id);
    }
//    public void updateStatus(int id, String status){
//        Movie m = movieRepository.getById(id);
//        m.setStatus(status);
//        movieRepository.save(m);
//    }
}
