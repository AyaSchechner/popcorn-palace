package com.att.tdp.popcorn_palace.movieService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.tdp.popcorn_palace.movieService.model.Movie;
import com.att.tdp.popcorn_palace.movieService.repository.MovieRepository;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    // Add a new movie
    public Movie addMovie(Movie movie) {
        if (movie == null) {
            throw new IllegalArgumentException("Movie cannot be null.");
        }
        
        String validationError = validateMovieOrReturnError(movie);
        if (validationError != null) {
            throw new IllegalArgumentException(validationError);
        }

        Optional<Movie> existing = movieRepository.findByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Movie with title '" + movie.getTitle() + "' from " + movie.getReleaseYear() + " already exists.");
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

    // Update an existing movie by title
    public Optional<Movie> updateMovie(String movieTitle, Movie updatedMovie) {
        if (updatedMovie == null) {
            System.out.println("Updated movie data is null.");
            return null;
        }
    
        Optional<Movie> optionalMovie = movieRepository.findByTitle(movieTitle);

        if (optionalMovie.isEmpty()) return Optional.empty(); 

        String validationError = validateMovieOrReturnError(updatedMovie);
        if (validationError != null) {
            throw new IllegalArgumentException(validationError); 
        }

        Movie movie = optionalMovie.get();
        movie.setTitle(updatedMovie.getTitle());
        movie.setGenre(updatedMovie.getGenre());
        movie.setDuration(updatedMovie.getDuration());
        movie.setRating(updatedMovie.getRating());
        movie.setReleaseYear(updatedMovie.getReleaseYear());

        return Optional.of(movieRepository.save(movie));        
    }

    // Delete a movie by ID
    public boolean deleteMovieByTitle(String movieTitle) {
        Optional<Movie> optionalMovie = movieRepository.findByTitle(movieTitle);
        if (optionalMovie.isEmpty()) {
            return false; 
        }
        movieRepository.delete(optionalMovie.get());
        return true;
    }
    
    // Validate movie before saving. Return error message if validation fails, otherwise return null
    private String validateMovieOrReturnError(Movie movie) {
        if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) return "Title cannot be empty.";
        if (movie.getGenre() == null || movie.getGenre().trim().isEmpty()) return "Genre cannot be empty.";
        if (movie.getDuration() <= 0) return "Duration must be greater than 0.";
        if (movie.getRating() < 0 || movie.getRating() > 10) return "Rating must be between 0 and 10.";
        if (movie.getReleaseYear() < 1895 || movie.getReleaseYear() > 2025) return "Release year must be between 1895 and 2025.";
        return null; // No errors
    }
}
