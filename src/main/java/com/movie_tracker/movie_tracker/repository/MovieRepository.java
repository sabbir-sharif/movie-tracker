package com.movie_tracker.movie_tracker.repository;

import com.movie_tracker.movie_tracker.models.Movie;
import com.movie_tracker.movie_tracker.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Page<Movie> findByUser(User user, Pageable pageable);
}
