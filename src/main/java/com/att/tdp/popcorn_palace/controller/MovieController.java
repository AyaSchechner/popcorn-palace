package com.att.tdp.popcorn_palace.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.att.tdp.popcorn_palace.model.Movie;
import com.att.tdp.popcorn_palace.service.MovieService;

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
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Update movie by title
    @PostMapping("/update/{movieTitle}")
    public ResponseEntity<?> updateMovie(@PathVariable String movieTitle, @RequestBody Movie updatedMovie) {
        try {
            Movie movie = movieService.updateMovie(movieTitle, updatedMovie);
            return ResponseEntity.ok(movie); // Return the updated movie with 200 OK status
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Delete a movie by title
    @DeleteMapping("/{movieTitle}")
    public ResponseEntity<?> deleteMovie(@PathVariable String movieTitle) {
        try {
            if (movieService.deleteMovieByTitle(movieTitle)) {
                return ResponseEntity.ok("Movie deleted successfully.");
            }
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
        return createErrorResponse(HttpStatus.NOT_FOUND, "Movie not found.");
    }


    // Helper Method: Create a structured JSON error response
    private ResponseEntity<Map<String, String>> createErrorResponse(HttpStatus status, String message) {
        Map<String, String> error = new HashMap<>();
        error.put("error", message);
        return ResponseEntity.status(status).body(error);
    }
}
