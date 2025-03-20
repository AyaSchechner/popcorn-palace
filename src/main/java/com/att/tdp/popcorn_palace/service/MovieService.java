package com.att.tdp.popcorn_palace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.repository.MovieRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;


    // Validate movie before saving
    private String validateMovie(Movie movie) {
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
            return "Title cannot be empty.";
        }
        if (movie.getGenre() == null || movie.getGenre().trim().isEmpty()) {
            return "Genre cannot be empty.";
        }
        if (movie.getDuration() <= 0) {
            return "Duration must be greater than 0.";
        }
        if (movie.getRating() < 0 || movie.getRating() > 10) {
            return "Rating must be between 0 and 10.";
        }
        if (movie.getReleaseYear() < 1895 || movie.getReleaseYear() > 2025) {
            return "Release year must be between 1895 and 2025.";
        }
        return null; // No errors
    }

    // Add a new movie
    public Movie addMovie(Movie movie) {
        String validationError = validateMovie(movie);
        if (validationError != null) {
            throw new RuntimeException(validationError);
        }
        return movieRepository.save(movie);
    }

    // Get all movies
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    // Get a movie by id
    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    // Update an existing movie
    public Movie updateMovie(Long id, Movie updatedMovie) {
        Optional<Movie> existingMovie = movieRepository.findById(id);
        if (existingMovie.isPresent()) {
            String validationError = validateMovie(updatedMovie);
            if (validationError != null) {
                throw new RuntimeException(validationError);
            }
            Movie movie = existingMovie.get();
            movie.setTitle(updatedMovie.getTitle());
            movie.setGenre(updatedMovie.getGenre());
            movie.setDuration(updatedMovie.getDuration());
            movie.setRating(updatedMovie.getRating());
            movie.setReleaseYear(updatedMovie.getReleaseYear());
            return movieRepository.save(movie);
        } else {
            throw new RuntimeException("Movie not found.");
        }
    }

    // Delete a movie by ID
    public boolean deleteMovie(Long id) {
        if (movieRepository.existsById(id)) {
            movieRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
