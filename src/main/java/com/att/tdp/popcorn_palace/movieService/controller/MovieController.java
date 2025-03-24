package com.att.tdp.popcorn_palace.movieService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.att.tdp.popcorn_palace.movieService.model.Movie;
import com.att.tdp.popcorn_palace.movieService.service.MovieService;

import java.util.*;

@RestController
@RequestMapping("/movies")
public class MovieController {
    
    @Autowired
    private MovieService movieService;

    // Add a new movie
    @PostMapping
    public ResponseEntity<?> addMovie(@RequestBody Movie movie) {
        try {
            Movie savedMovie = movieService.addMovie(movie);
            return ResponseEntity.ok(savedMovie);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Get all movies
    @GetMapping("/all")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.ok(movies);
    }

    // Get a movie by id
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {          
        return movieService.getMovieById(id)
        .<ResponseEntity<?>>map(ResponseEntity::ok)
        .orElseGet(() -> createErrorResponse(HttpStatus.NOT_FOUND, "Movie not found."));
    }

    // Update movie by title
    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<?> updateMovie(@PathVariable String movieTitle, @RequestBody Movie updatedMovie) {
        if (updatedMovie == null) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Request body for movie update is missing.");
        }
    
        try {
            Optional<Movie> movie = movieService.updateMovie(movieTitle, updatedMovie);
            if (movie.isEmpty()) {
                return createErrorResponse(HttpStatus.NOT_FOUND, "Movie with title '" + movieTitle + "' not found.");
            }
            return ResponseEntity.ok().build(); // 200 OK with no body
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Delete a movie by title
    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<?> deleteMovie(@PathVariable String movieTitle) {
        boolean deleted = movieService.deleteMovieByTitle(movieTitle);
        if (deleted) {
            return ResponseEntity.ok().build(); // no body on success
        }
        return createErrorResponse(HttpStatus.NOT_FOUND, "Movie not found.");
    }

    // Creates a structured JSON error response
    private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }
}
