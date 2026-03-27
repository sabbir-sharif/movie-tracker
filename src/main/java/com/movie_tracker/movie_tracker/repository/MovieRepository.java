package com.movie_tracker.movie_tracker.repository;

import com.movie_tracker.movie_tracker.models.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
}
