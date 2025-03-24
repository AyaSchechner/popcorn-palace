package com.att.tdp.popcorn_palace.movieService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.att.tdp.popcorn_palace.movieService.model.Movie;

import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitle(String title); 
    Optional<Movie> findByTitleAndReleaseYear(String title, int releaseYear);

}
